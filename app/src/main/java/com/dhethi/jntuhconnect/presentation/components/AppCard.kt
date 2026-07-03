package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.ShapeLg

/**
 * Standard tonal card used across the app. Rounded, subtly elevated, with a thin
 * outline for definition on both light and dark surfaces. Optionally clickable.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentPadding: PaddingValues = PaddingValues(Dimens.space),
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = CardDefaults.cardColors(
        containerColor = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface
    )
    val elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationSm)

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = ShapeLg,
            colors = colors,
            elevation = elevation
        ) {
            Column(Modifier.fillMaxWidth().padding(contentPadding), content = content)
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = ShapeLg,
            colors = colors,
            elevation = elevation
        ) {
            Column(Modifier.fillMaxWidth().padding(contentPadding), content = content)
        }
    }
}
