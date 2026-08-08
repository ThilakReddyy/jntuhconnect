package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.Shape
import com.dhethi.jntuhconnect.presentation.theme.ShapeMd

/**
 * Animated pill segmented control. The selected segment fills with the primary color
 * and its label animates in. Evenly distributes segments across the full width.
 */
@Composable
fun SegmentedTabs(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val largeText = LocalDensity.current.fontScale > 1.2f
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .then(if (largeText) Modifier.horizontalScroll(rememberScrollState()) else Modifier)
            .padding(Dimens.spaceXs),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spaceXs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            val container by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                animationSpec = tween(220),
                label = "segBg"
            )
            val content by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                animationSpec = tween(220),
                label = "segFg"
            )
            Box(
                modifier = (if (largeText) Modifier.widthIn(min = 112.dp) else Modifier.weight(1f))
                    .heightIn(min = Dimens.touchTarget)
                    .clip(ShapeMd)
                    .background(container)
                    .selectable(
                        selected = isSelected,
                        role = Role.Tab,
                        onClick = { if (!isSelected) onSelect(option) }
                    )
                    .padding(vertical = Dimens.spaceSm, horizontal = Dimens.spaceSm),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = content,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    maxLines = 1
                )
            }
        }
    }
}
