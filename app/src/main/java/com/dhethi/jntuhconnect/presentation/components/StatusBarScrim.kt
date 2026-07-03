package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

/**
 * A solid scrim occupying the status-bar strip at the top of an edge-to-edge screen.
 *
 * Place it as the last child of a [Box] that also holds the scrolling content, and pass
 * [visible] = true once the header/hero has scrolled off. This keeps scrolling content from
 * bleeding under the system clock/date while leaving the header untouched at rest. Fades
 * in/out so the transition isn't a hard pop.
 */
@Composable
fun BoxScope.StatusBarScrim(visible: Boolean) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(200),
        label = "statusBarScrimAlpha"
    )
    if (alpha > 0f) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .alpha(alpha)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
