package com.dhethi.jntuhconnect.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Central spacing / radius / elevation tokens. Using these everywhere keeps paddings,
 * corner radii and component sizes consistent across the whole app.
 */
object Dimens {
    // Spacing scale
    val spaceXxs = 2.dp
    val spaceXs = 4.dp
    val spaceSm = 8.dp
    val spaceMd = 12.dp
    val space = 16.dp
    val spaceLg = 20.dp
    val spaceXl = 24.dp
    val spaceXxl = 32.dp
    val spaceHuge = 48.dp

    // Corner radii
    val radiusSm = 10.dp
    val radiusMd = 14.dp
    val radius = 18.dp
    val radiusLg = 24.dp
    val radiusXl = 30.dp

    // Elevation
    val elevationSm = 1.dp
    val elevation = 3.dp
    val elevationLg = 8.dp

    // Common sizes
    val touchTarget = 48.dp
    val iconSm = 18.dp
    val icon = 24.dp
    val iconLg = 32.dp
    val avatar = 46.dp
    val heroRing = 96.dp
}

/** Rounded shapes derived from [Dimens]. */
val ShapeSm = RoundedCornerShape(Dimens.radiusSm)
val ShapeMd = RoundedCornerShape(Dimens.radiusMd)
val Shape = RoundedCornerShape(Dimens.radius)
val ShapeLg = RoundedCornerShape(Dimens.radiusLg)
val ShapeXl = RoundedCornerShape(Dimens.radiusXl)

/**
 * The signature emerald→teal brand gradient, mode-aware. Use for hero headers,
 * primary CTAs and accent surfaces.
 */
@Composable
@ReadOnlyComposable
fun brandGradient(dark: Boolean): Brush = Brush.linearGradient(
    colors = if (dark) {
        listOf(brandGradientDarkStart, brandGradientDarkMid, brandGradientDarkEnd)
    } else {
        listOf(brandGradientLightStart, brandGradientLightMid, brandGradientLightEnd)
    }
)

@Composable
@ReadOnlyComposable
fun brandGradientStatusBar(dark: Boolean): Brush = Brush.linearGradient(
    colors = if (dark) {
        listOf(brandStatusGradientDarkStart, brandStatusGradientDarkMid, brandStatusGradientDarkEnd)
    } else {
        listOf(brandStatusGradientLightStart, brandStatusGradientLightMid, brandStatusGradientLightEnd)
    }
)

/** Vertical variant used for tall hero surfaces. */
@Composable
@ReadOnlyComposable
fun brandGradientVertical(dark: Boolean): Brush = Brush.verticalGradient(
    colors = if (dark) {
        listOf(brandGradientDarkStart, brandGradientDarkEnd)
    } else {
        listOf(brandGradientLightStart, brandGradientLightEnd)
    }
)

/** Soft translucent white overlay for glassy chips on top of the gradient. */
val glassOnBrand = Color(0x33FFFFFF)
val glassOnBrandStrong = Color(0x4DFFFFFF)
