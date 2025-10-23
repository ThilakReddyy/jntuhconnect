package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers

@Composable
fun ErrorScreen(
    errorMessage: String?,
    refreshPage: () -> Unit
) {
    val context = LocalContext.current
    var internetAvailable by remember { mutableStateOf(true) }
    var serverAvailable by remember { mutableStateOf(true) }

    // Run checks in background when screen loads
    LaunchedEffect(Unit) {
        internetAvailable = isInternetAvailable(context)
        if (internetAvailable) {
            serverAvailable = kotlinx.coroutines.withContext(Dispatchers.IO) {
                isServerReachable()
            }
        }
    }

    when {
        !internetAvailable -> {
            InternetNotAvailable(
                refreshPage = {
                    internetAvailable = isInternetAvailable(context)
                    refreshPage()
                }
            )
        }

        !serverAvailable -> {
            ServerNotAvailable(
                refreshPage = {
                    serverAvailable = isServerReachable()
                    if (serverAvailable) refreshPage()
                }
            )
        }

        else -> {
            GenericErrorScreen(
                errorMessage = errorMessage,
                refreshPage = refreshPage
            )
        }
    }
}
