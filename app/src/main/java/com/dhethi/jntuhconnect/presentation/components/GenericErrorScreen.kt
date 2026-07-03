package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.runtime.Composable

@Composable
fun GenericErrorScreen(
    errorMessage: String? = null,
    refreshPage: () -> Unit
) {
    ErrorStateScreen(
        icon = Icons.Filled.ErrorOutline,
        title = "Something Went Wrong",
        message = errorMessage ?: "An unexpected error occurred. Please try again.",
        tips = listOf(
            "Try refreshing the page",
            "Check your internet connection",
            "Restart the app if the problem persists"
        ),
        onRetry = refreshPage
    )
}
