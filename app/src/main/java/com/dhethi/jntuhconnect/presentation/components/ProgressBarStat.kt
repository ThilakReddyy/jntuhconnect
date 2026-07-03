package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.presentation.theme.brandGradientLightEnd
import com.dhethi.jntuhconnect.presentation.theme.brandGradientLightStart

/**
 * Labeled linear progress bar with a trailing "obtained / total" caption. Used for
 * credit-completion per academic year and overall.
 */
@Composable
fun ProgressBarStat(
    label: String,
    obtained: Float,
    total: Float,
    modifier: Modifier = Modifier,
    trailingText: String = "${format(obtained)} / ${format(total)}",
    barColors: List<Color> = listOf(brandGradientLightStart, brandGradientLightEnd)
) {
    val fraction = if (total > 0f) (obtained / total).coerceIn(0f, 1f) else 0f
    val animated by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(700),
        label = "barProgress"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = trailingText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animated)
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Brush.horizontalGradient(barColors))
            )
        }
    }
}

private fun format(v: Float): String =
    if (v % 1f == 0f) v.toInt().toString() else v.toString()
