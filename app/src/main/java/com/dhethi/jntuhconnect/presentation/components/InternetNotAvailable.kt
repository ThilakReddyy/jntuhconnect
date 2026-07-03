package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiBad
import androidx.compose.runtime.Composable

@Composable
fun InternetNotAvailable(refreshPage: () -> Unit) {
    ErrorStateScreen(
        icon = Icons.Filled.SignalWifiBad,
        title = "No Internet Connection",
        message = "Please check your network connection and try again.",
        tips = listOf(
            "Check your WiFi or mobile data connection",
            "Try turning airplane mode on and off",
            "Move to an area with better signal"
        ),
        onRetry = refreshPage
    )
}
