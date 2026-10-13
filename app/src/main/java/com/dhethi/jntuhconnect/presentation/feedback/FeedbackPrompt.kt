package com.dhethi.jntuhconnect.presentation.feedback

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.dhethi.jntuhconnect.domain.model.FeedbackDestination
import com.dhethi.jntuhconnect.domain.model.feedbackDestination
import kotlinx.coroutines.delay

private const val FEEDBACK_EMAIL = "thilakreddy@dhethi.com"

/** Mounted at the app root; prompts only while Home is resumed and settled. */
@Composable
fun FeedbackPrompt(
    isHome: Boolean,
    otherPromptVisible: Boolean,
    viewModel: FeedbackPromptViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleState by lifecycle.currentStateFlow.collectAsState()
    val state by viewModel.state.collectAsState()
    val visible by viewModel.visible.collectAsState()
    val context = LocalContext.current
    var selectedRating by rememberSaveable { mutableIntStateOf(0) }
    var returnedFromExternal by rememberSaveable { mutableStateOf(false) }
    var launchError by rememberSaveable { mutableStateOf<String?>(null) }

    val externalLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // RESULT_OK does not prove a review was posted or an email was sent.
        returnedFromExternal = true
    }

    LaunchedEffect(isHome, otherPromptVisible, lifecycle) {
        if (!isHome || otherPromptVisible) return@LaunchedEffect
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            // Follow notification onboarding (5s) and the update check (12s).
            delay(20_000)
            viewModel.showIfDue()
        }
    }

    LaunchedEffect(visible) {
        if (!visible) {
            selectedRating = 0
            returnedFromExternal = false
            launchError = null
        }
    }

    if (!visible || state?.completed != false || !isHome || otherPromptVisible ||
        lifecycleState != Lifecycle.State.RESUMED
    ) return

    val rating = state?.pendingRating
    if (rating == null) {
        AlertDialog(
            onDismissRequest = viewModel::defer,
            title = { Text("How is JNTUH Connect?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Rate your experience from 1 to 5 stars.")
                    Row(
                        modifier = Modifier.fillMaxWidth().selectableGroup(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        (1..5).forEach { value ->
                            Icon(
                                imageVector = if (value <= selectedRating) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                                contentDescription = "$value ${if (value == 1) "star" else "stars"}",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f).heightIn(min = 48.dp).selectable(
                                    selected = selectedRating == value,
                                    role = Role.RadioButton,
                                    onClick = { selectedRating = value }
                                )
                            )
                        }
                    }
                    TextButton(onClick = viewModel::complete) {
                        Text("I've already shared feedback")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = selectedRating in 1..5,
                    onClick = { viewModel.selectRating(selectedRating) }
                ) { Text("Continue") }
            },
            dismissButton = { TextButton(onClick = viewModel::defer) { Text("Not now") } }
        )
        return
    }

    val destination = feedbackDestination(rating)
    val isPlayStore = destination == FeedbackDestination.PLAY_STORE
    AlertDialog(
        onDismissRequest = viewModel::defer,
        title = { Text(if (returnedFromExternal) "Did you share your feedback?" else if (isPlayStore) "Rate us on Google Play" else "What can we improve?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    when {
                        returnedFromExternal -> "If you've already posted a review or sent your feedback, let us know so we won't ask again."
                        isPlayStore -> "Thanks for your feedback. Would you like to share an honest rating or review on Google Play?"
                        else -> "Tell us what we can do better. Write to $FEEDBACK_EMAIL."
                    }
                )
                launchError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                if (!returnedFromExternal) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = openFeedback@{
                            launchError = null
                            val intent = if (isPlayStore) {
                                playStoreIntent(context.packageName)
                            } else {
                                feedbackEmailIntent(rating)
                            }
                            // Some Android versions silently cancel an email intent without
                            // a handler. The manifest query makes this preflight visible.
                            if (!isPlayStore && context.packageManager.queryIntentActivities(
                                    intent, PackageManager.MATCH_DEFAULT_ONLY
                                ).isEmpty()
                            ) {
                                launchError = "No email app is available. You can write to $FEEDBACK_EMAIL."
                                return@openFeedback
                            }
                            try {
                                externalLauncher.launch(intent)
                            } catch (_: ActivityNotFoundException) {
                                if (isPlayStore) {
                                    try {
                                        externalLauncher.launch(playStoreIntent(context.packageName, web = true))
                                    } catch (_: ActivityNotFoundException) {
                                        launchError = "Couldn't open Google Play. Please try again later."
                                    }
                                } else {
                                    launchError = "No email app is available. You can write to $FEEDBACK_EMAIL."
                                }
                            }
                        }
                    ) { Text(if (isPlayStore) "Open Google Play" else "Write feedback") }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = viewModel::complete) {
                Text(if (returnedFromExternal) "Yes, I've shared it" else "I've already shared feedback")
            }
        },
        dismissButton = { TextButton(onClick = viewModel::defer) { Text("Not now") } }
    )
}

private fun playStoreIntent(packageName: String, web: Boolean = false): Intent {
    val uri = if (web) "https://play.google.com/store/apps/details?id=$packageName"
        else "market://details?id=$packageName"
    return Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
        if (!web) setPackage("com.android.vending")
    }
}

private fun feedbackEmailIntent(rating: Int): Intent {
    val subject = Uri.encode("JNTUH Connect feedback")
    val body = Uri.encode("My experience: $rating/5 stars\n\nWhat could be improved:\n")
    return Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$FEEDBACK_EMAIL?subject=$subject&body=$body"))
}
