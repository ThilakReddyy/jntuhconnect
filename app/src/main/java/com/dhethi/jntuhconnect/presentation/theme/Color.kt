package com.dhethi.jntuhconnect.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Material 3 color roles for JNTUH Connect — "Academic Emerald" identity.
 *
 * The brand is built around a fresh emerald→teal gradient with warm amber accents.
 * Every role is defined for BOTH light and dark so nothing falls back to the default
 * M3 purple. Decorative gradient stops and semantic grade colors live at the bottom.
 */

// ---------- Light ----------
val md_light_primary = Color(0xFF0E9F6E)
val md_light_onPrimary = Color(0xFFFFFFFF)
val md_light_primaryContainer = Color(0xFFB9F2D8)
val md_light_onPrimaryContainer = Color(0xFF00351F)

val md_light_secondary = Color(0xFF0F766E)
val md_light_onSecondary = Color(0xFFFFFFFF)
val md_light_secondaryContainer = Color(0xFFB9ECE6)
val md_light_onSecondaryContainer = Color(0xFF00201D)

val md_light_tertiary = Color(0xFFB4791C)
val md_light_onTertiary = Color(0xFFFFFFFF)
val md_light_tertiaryContainer = Color(0xFFFFDFA6)
val md_light_onTertiaryContainer = Color(0xFF2A1800)

val md_light_error = Color(0xFFBA1A1A)
val md_light_onError = Color(0xFFFFFFFF)
val md_light_errorContainer = Color(0xFFFFDAD6)
val md_light_onErrorContainer = Color(0xFF410002)

val md_light_background = Color(0xFFF3FBF6)
val md_light_onBackground = Color(0xFF0F1512)
val md_light_surface = Color(0xFFFFFFFF)
val md_light_onSurface = Color(0xFF0F1512)
val md_light_surfaceVariant = Color(0xFFDCE9E1)
val md_light_onSurfaceVariant = Color(0xFF404A44)
val md_light_outline = Color(0xFF6F7A73)
val md_light_outlineVariant = Color(0xFFBFCBC3)
val md_light_inverseSurface = Color(0xFF2B322E)
val md_light_inverseOnSurface = Color(0xFFEEF2ED)
val md_light_inversePrimary = Color(0xFF4ADE9E)
val md_light_scrim = Color(0xFF000000)

// Extra tonal surfaces for premium layered cards (not part of the base M3 scheme).
val md_light_surfaceContainer = Color(0xFFEDF6F0)
val md_light_surfaceContainerHigh = Color(0xFFE6F1EA)

// ---------- Dark ----------
val md_dark_primary = Color(0xFF4ADE9E)
val md_dark_onPrimary = Color(0xFF00382A)
val md_dark_primaryContainer = Color(0xFF0A6B4E)
val md_dark_onPrimaryContainer = Color(0xFFB9F2D8)

val md_dark_secondary = Color(0xFF54D6C8)
val md_dark_onSecondary = Color(0xFF003733)
val md_dark_secondaryContainer = Color(0xFF0E5B54)
val md_dark_onSecondaryContainer = Color(0xFFB9ECE6)

val md_dark_tertiary = Color(0xFFF5BE55)
val md_dark_onTertiary = Color(0xFF3F2D00)
val md_dark_tertiaryContainer = Color(0xFF6A4E12)
val md_dark_onTertiaryContainer = Color(0xFFFFDFA6)

val md_dark_error = Color(0xFFFFB4AB)
val md_dark_onError = Color(0xFF690005)
val md_dark_errorContainer = Color(0xFF93000A)
val md_dark_onErrorContainer = Color(0xFFFFDAD6)

val md_dark_background = Color(0xFF0A120E)
val md_dark_onBackground = Color(0xFFDCE5DE)
val md_dark_surface = Color(0xFF0F1814)
val md_dark_onSurface = Color(0xFFDCE5DE)
val md_dark_surfaceVariant = Color(0xFF1B2A24)
val md_dark_onSurfaceVariant = Color(0xFFBEC9C1)
val md_dark_outline = Color(0xFF88938B)
val md_dark_outlineVariant = Color(0xFF404A44)
val md_dark_inverseSurface = Color(0xFFDCE5DE)
val md_dark_inverseOnSurface = Color(0xFF2B322E)
val md_dark_inversePrimary = Color(0xFF0E9F6E)
val md_dark_scrim = Color(0xFF000000)

val md_dark_surfaceContainer = Color(0xFF16211C)
val md_dark_surfaceContainerHigh = Color(0xFF1E2A24)

// ---------- Brand gradient stops ----------
// Used by GradientHero and hero headers. Tuned per mode for legibility of white text.
val brandGradientLightStart = Color(0xFF10B981)
val brandGradientLightMid = Color(0xFF0EA5A5)
val brandGradientLightEnd = Color(0xFF0E7C86)

val brandGradientDarkStart = Color(0xFF0B7F5C)
val brandGradientDarkMid = Color(0xFF0C6E73)
val brandGradientDarkEnd = Color(0xFF0A5560)


val brandStatusGradientDarkStart = Color(0xFF0B7F5C)
val brandStatusGradientDarkMid = Color(0xFF0B736C)
val brandStatusGradientDarkEnd = Color(0xFF0B626A)

val brandStatusGradientLightStart = Color(0xFF10B981)
val brandStatusGradientLightMid = Color(0xFF0EA89E)
val brandStatusGradientLightEnd = Color(0xFF0E9196)


// Amber accent used sparingly for highlights / CTAs / "new" markers.
val accentAmber = Color(0xFFF59E0B)
val accentAmberSoft = Color(0xFFFFE8C2)

// ---------- Semantic grade colors ----------
// Tint result grades; tuned to read well on both light and dark surfaces.
val gradeOutstanding = Color(0xFF059669) // O
val gradeExcellent = Color(0xFF16A34A)   // A+, A
val gradeGood = Color(0xFF0284C7)        // B+, B
val gradeAverage = Color(0xFFD97706)     // C
val gradeBorderline = Color(0xFFEA580C)  // D
val gradeFail = Color(0xFFDC2626)        // F, Ab
