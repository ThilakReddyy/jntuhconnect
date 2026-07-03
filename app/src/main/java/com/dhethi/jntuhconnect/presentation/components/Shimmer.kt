package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.ShapeLg

/** An animated shimmer brush that sweeps across skeleton placeholders. */
@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )
    val base = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
    val colors = listOf(
        base.copy(alpha = 0.6f),
        base.copy(alpha = 0.25f),
        base.copy(alpha = 0.6f)
    )
    return Brush.linearGradient(
        colors = colors,
        start = Offset(translate - 300f, 0f),
        end = Offset(translate, 0f)
    )
}

/** A rounded shimmer block. */
@Composable
fun ShimmerBox(modifier: Modifier) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(shimmerBrush())
    )
}

/** A skeleton card used while result data loads. */
@Composable
fun ShimmerCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeLg)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainer)
            .padding(Dimens.space)
    ) {
        ShimmerBox(Modifier.fillMaxWidth(0.5f).height(18.dp))
        Spacer(Modifier.height(Dimens.spaceMd))
        repeat(3) {
            ShimmerBox(Modifier.fillMaxWidth().height(14.dp))
            Spacer(Modifier.height(Dimens.spaceSm))
        }
    }
}

/** A vertically stacked set of shimmer cards for full-screen loading. */
@Composable
fun ShimmerList(modifier: Modifier = Modifier, count: Int = 4) {
    Column(modifier = modifier.padding(Dimens.space)) {
        repeat(count) {
            ShimmerCard()
            Spacer(Modifier.height(Dimens.spaceMd))
        }
    }
}
