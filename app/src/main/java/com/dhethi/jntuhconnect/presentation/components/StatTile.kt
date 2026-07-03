package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.ShapeMd
import com.dhethi.jntuhconnect.presentation.theme.glassOnBrand

/**
 * A compact "value over label" statistic tile. Two flavours:
 *  - default tonal surface (used inside cards)
 *  - [onBrand] = true → glassy translucent style for use on the gradient hero.
 */
@Composable
fun StatTile(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    onBrand: Boolean = false,
    valueColor: Color? = null
) {
    val container = if (onBrand) glassOnBrand else MaterialTheme.colorScheme.surfaceContainerHigh
    val resolvedValueColor = valueColor
        ?: if (onBrand) Color.White else MaterialTheme.colorScheme.onSurface
    val labelColor = if (onBrand) Color.White.copy(alpha = 0.8f)
    else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .clip(ShapeMd)
            .background(container)
            .padding(vertical = Dimens.spaceMd, horizontal = Dimens.spaceSm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = resolvedValueColor,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = labelColor,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
