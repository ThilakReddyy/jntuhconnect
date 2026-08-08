package com.dhethi.jntuhconnect.presentation.update

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "InAppUpdate"

/**
 * Drives Google Play's flexible in-app update flow.
 *
 * When a newer version is live on the Play Store, this offers an in-app action. Play's download
 * consent sheet opens only after the user taps it. The update downloads in the background while
 * the app stays usable; once ready, a snackbar invites the user to restart and install. Only
 * fires for Play-installed builds — debug/sideloaded installs are silently skipped.
 *
 * Mount once, high in the tree, and pass the [SnackbarHostState] used by the app's Scaffold.
 */
@Composable
fun InAppUpdateHandler(snackbarHostState: SnackbarHostState) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val appUpdateManager = remember { AppUpdateManagerFactory.create(context.applicationContext) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var updateCheckInFlight by remember { mutableStateOf(false) }
    var updateFlowRequested by rememberSaveable { mutableStateOf(false) }
    var updatePromptShown by rememberSaveable { mutableStateOf(false) }
    var restartPromptVisible by remember { mutableStateOf(false) }

    val updateLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        // User dismissed or the flow failed — nothing to do, we'll ask again next launch.
        Log.d(TAG, "Update flow result: ${result.resultCode}")
    }

    suspend fun promptRestart() {
        if (restartPromptVisible) return
        restartPromptVisible = true
        try {
            val action = snackbarHostState.showSnackbar(
                message = "An update has been downloaded.",
                actionLabel = "Restart",
                duration = SnackbarDuration.Indefinite
            )
            if (action == SnackbarResult.ActionPerformed) {
                appUpdateManager.completeUpdate()
                    .addOnFailureListener { Log.w(TAG, "completeUpdate failed", it) }
            }
        } finally {
            restartPromptVisible = false
        }
    }

    // Surface the restart prompt the moment a background download finishes.
    DisposableEffect(appUpdateManager) {
        val listener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                scope.launch { promptRestart() }
            }
        }
        appUpdateManager.registerListener(listener)
        onDispose { appUpdateManager.unregisterListener(listener) }
    }

    suspend fun promptUpdate(info: com.google.android.play.core.appupdate.AppUpdateInfo) {
        val action = snackbarHostState.showSnackbar(
            message = "A new version is available.",
            actionLabel = "Update",
            duration = SnackbarDuration.Long
        )
        if (
            action != SnackbarResult.ActionPerformed ||
            lifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED
        ) {
            return
        }

        updateFlowRequested = true
        try {
            if (!appUpdateManager.startFlexibleUpdate(info, updateLauncher)) {
                updateFlowRequested = false
                Log.w(TAG, "Play declined to start the flexible update flow")
            }
        } catch (error: IllegalStateException) {
            updateFlowRequested = false
            Log.w(TAG, "Unable to start flexible update", error)
        }
    }

    // ColorOS devices can report an input ANR when a system-owned update window races the app's
    // first focused frame. Delay the check and only open Play UI after an explicit user action.
    DisposableEffect(lifecycleOwner, appUpdateManager) {
        var effectActive = true
        var resumeCheckJob: Job? = null

        fun checkForUpdate() {
            if (updateCheckInFlight || updateFlowRequested || updatePromptShown) return
            updateCheckInFlight = true
            appUpdateManager.appUpdateInfo
                .addOnSuccessListener { info ->
                    if (
                        !effectActive ||
                        lifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED
                    ) {
                        return@addOnSuccessListener
                    }
                    when {
                        info.installStatus() == InstallStatus.DOWNLOADED ->
                            scope.launch { promptRestart() }

                        info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                            info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> {
                            updatePromptShown = true
                            scope.launch { promptUpdate(info) }
                        }
                    }
                }
                .addOnFailureListener { Log.d(TAG, "appUpdateInfo failed", it) }
                .addOnCompleteListener {
                    if (effectActive) updateCheckInFlight = false
                }
        }

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    resumeCheckJob?.cancel()
                    resumeCheckJob = scope.launch {
                        delay(3_000)
                        if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                            checkForUpdate()
                        }
                    }
                }

                Lifecycle.Event.ON_PAUSE -> {
                    resumeCheckJob?.cancel()
                    resumeCheckJob = null
                }

                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            effectActive = false
            resumeCheckJob?.cancel()
            updateCheckInFlight = false
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

private fun AppUpdateManager.startFlexibleUpdate(
    info: com.google.android.play.core.appupdate.AppUpdateInfo,
    launcher: androidx.activity.result.ActivityResultLauncher<androidx.activity.result.IntentSenderRequest>
): Boolean =
    startUpdateFlowForResult(
        info,
        launcher,
        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
    )
