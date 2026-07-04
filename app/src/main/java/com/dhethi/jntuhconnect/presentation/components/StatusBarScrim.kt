package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

/**
 * A solid fill occupying the status-bar strip at the top of an edge-to-edge screen.
 *
 * Place it as the last child of a [Box] that also holds the scrolling content. It keeps
 * scrolling content from bleeding under the system clock/date. Pass [brush] from the call
 * site so the screen decides the fill — e.g. the hero gradient while the hero is at rest,
 * and a solid background once the hero has scrolled off.
 */
@Composable
fun BoxScope.StatusBarScrim(brush: Brush) {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .background(brush)
    )
}
