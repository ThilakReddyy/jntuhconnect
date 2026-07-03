package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.runtime.Composable

@Composable
fun ServerNotAvailable(refreshPage: () -> Unit) {
    ErrorStateScreen(
        icon = Icons.Filled.CloudOff,
        title = "Server Not Reachable",
        message = "We’re unable to connect to our servers right now. Please try again later.",
        tips = listOf(
            "Wait for a few minutes and try again",
            "Ensure you are using the correct server URL",
            "Contact support if the issue persists"
        ),
        onRetry = refreshPage
    )
}
