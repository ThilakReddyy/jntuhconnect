package com.dhethi.jntuhconnect.presentation.notificationpreferences

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dhethi.jntuhconnect.common.ContentData
import com.dhethi.jntuhconnect.presentation.components.AppCard
import com.dhethi.jntuhconnect.presentation.components.AppTopBar
import com.dhethi.jntuhconnect.presentation.components.ChoiceChip
import com.dhethi.jntuhconnect.presentation.components.FilterGroup
import com.dhethi.jntuhconnect.presentation.components.PrimaryButton
import com.dhethi.jntuhconnect.presentation.components.TonalButton
import com.dhethi.jntuhconnect.presentation.theme.Dimens

@Composable
fun NotificationPreferencesScreen(
    viewModel: NotificationPreferencesViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    var notificationPermissionGranted by remember {
        mutableStateOf(hasNotificationPermission(context))
    }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        notificationPermissionGranted = granted
        if (granted) {
            viewModel.setNotificationsEnabled(true)
        } else {
            viewModel.onNotificationPermissionDenied()
        }
    }

    LaunchedEffect(notificationPermissionGranted, state.notificationsEnabled) {
        if (!notificationPermissionGranted && state.notificationsEnabled) {
            viewModel.setNotificationsEnabled(false)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(title = "Notification preferences", onBack = navigateBack)
        }
    ) { padding ->
        if (state.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.space)
        ) {
            Spacer(Modifier.height(Dimens.spaceMd))
            NotificationEnabledCard(
                enabled = state.notificationsEnabled && notificationPermissionGranted,
                updating = state.updatingEnabled,
                onEnabledChange = { enabled ->
                    if (
                        enabled &&
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        !notificationPermissionGranted
                    ) {
                        viewModel.markNotificationPermissionRequested()
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.setNotificationsEnabled(enabled)
                    }
                }
            )
            Spacer(Modifier.height(Dimens.spaceLg))

            Text(
                "Leave a category empty to get every result release. Pick specific " +
                    "degrees or regulations to only get notified for those.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(Dimens.spaceLg))

            FilterGroup("Degree") {
                ContentData.degrees.forEach { label ->
                    val code = ContentData.degreeCodes[label] ?: return@forEach
                    ChoiceChip(label, code in state.degrees) { viewModel.toggleDegree(code) }
                }
            }
            FilterGroup("Regulation") {
                ContentData.regulations.forEach { (code, _) ->
                    ChoiceChip(code, code in state.regulations) { viewModel.toggleRegulation(code) }
                }
            }

            if (state.error != null) {
                Spacer(Modifier.height(Dimens.spaceSm))
                Text(
                    state.error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(Dimens.spaceLg))
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceMd)) {
                TonalButton(
                    text = "Reset to all",
                    onClick = viewModel::resetToAll,
                    enabled = !state.saving,
                    modifier = Modifier.weight(1f)
                )
                PrimaryButton(
                    text = "Save",
                    onClick = viewModel::save,
                    loading = state.saving,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(Dimens.spaceLg))
        }
    }
}

@Composable
private fun NotificationEnabledCard(
    enabled: Boolean,
    updating: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(Dimens.icon)
            )
            Spacer(Modifier.width(Dimens.spaceMd))
            Column(Modifier.weight(1f)) {
                Text(
                    "Result alerts",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Get notified about result releases and important updates",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(Dimens.spaceMd))
            Switch(
                checked = enabled,
                onCheckedChange = onEnabledChange,
                enabled = !updating
            )
        }
    }
}

private fun hasNotificationPermission(context: android.content.Context): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
