package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.presentation.theme.Dimens

/**
 * Animated circular progress ring with a value + caption in the center. Used for CGPA
 * (out of 10) and credit-completion percentages. Colors default to a white ring for use
 * on the gradient hero; pass explicit colors to reuse on tonal surfaces.
 */
@Composable
fun CgpaRing(
    progress: Float,               // 0f..1f
    centerValue: String,
    caption: String,
    modifier: Modifier = Modifier,
    size: Dp = Dimens.heroRing,
    trackColor: Color = Color.White.copy(alpha = 0.25f),
    progressColors: List<Color> = listOf(Color.White, Color.White),
    valueColor: Color = Color.White,
    captionColor: Color = Color.White.copy(alpha = 0.85f),
    strokeWidth: Dp = 8.dp
) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 900),
        label = "ringProgress"
    )
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = strokeWidth.toPx()
            val inset = stroke / 2
            val arcSize = androidx.compose.ui.geometry.Size(
                width = this.size.width - stroke,
                height = this.size.height - stroke
            )
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(progressColors),
                startAngle = -90f,
                sweepAngle = 360f * animated,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = centerValue,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Text(
                text = caption,
                style = MaterialTheme.typography.labelSmall,
                color = captionColor
            )
        }
    }
}
