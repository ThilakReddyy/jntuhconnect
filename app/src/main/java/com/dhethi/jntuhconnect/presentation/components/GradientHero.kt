package com.dhethi.jntuhconnect.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dhethi.jntuhconnect.presentation.theme.Dimens
import com.dhethi.jntuhconnect.presentation.theme.brandGradient

/**
 * Signature emerald→teal gradient surface used for screen headers and hero cards.
 * Content is laid out on top; text should use white / [Color.White] with alpha.
 */
@Composable
fun GradientHero(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(
        bottomStart = Dimens.radiusXl,
        bottomEnd = Dimens.radiusXl
    ),
    contentPadding: PaddingValues = PaddingValues(Dimens.spaceLg),
    content: @Composable () -> Unit
) {
    val dark = isSystemInDarkTheme()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(brandGradient(dark))
            .padding(contentPadding)
    ) {
        content()
    }
}

/** Rounded gradient hero card with all corners rounded, for embedding inside a scroll. */
@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(Dimens.spaceLg),
    content: @Composable () -> Unit
) {
    val dark = isSystemInDarkTheme()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.radiusLg))
            .background(brandGradient(dark))
            .padding(contentPadding)
    ) {
        content()
    }
}
