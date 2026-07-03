package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ErrorScreen(
    errorMessage: String?,
    refreshPage: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var internetAvailable by remember { mutableStateOf(true) }
    var serverAvailable by remember { mutableStateOf(true) }

    // Diagnose the failure once when the screen appears. The server probe is
    // network I/O, so it must run off the main thread.
    LaunchedEffect(Unit) {
        internetAvailable = isInternetAvailable(context)
        if (internetAvailable) {
            serverAvailable = withContext(Dispatchers.IO) { isServerReachable() }
        }
    }

    when {
        !internetAvailable -> InternetNotAvailable(
            refreshPage = {
                internetAvailable = isInternetAvailable(context)
                if (internetAvailable) refreshPage()
            }
        )

        !serverAvailable -> ServerNotAvailable(
            refreshPage = {
                scope.launch {
                    serverAvailable = withContext(Dispatchers.IO) { isServerReachable() }
                    if (serverAvailable) refreshPage()
                }
            }
        )

        else -> GenericErrorScreen(
            errorMessage = errorMessage,
            refreshPage = refreshPage
        )
    }
}
