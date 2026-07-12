package com.dhethi.jntuhconnect.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Material 3 color roles for JNTUH Connect — "Obsidian Academic" identity.
 *
 * The palette is intentionally near-monochrome rather than pure black and white:
 * ink, charcoal, graphite, smoke and soft ivory form the base, while restrained
 * blue-grey and warm-stone tones preserve hierarchy. Semantic result colors remain
 * distinct, but are muted so they sit naturally inside the system.
 */

// ---------- Light: soft paper and graphite ----------
val md_light_primary = Color(0xFF272B31)
val md_light_onPrimary = Color(0xFFFFFFFF)
val md_light_primaryContainer = Color(0xFFDDE1E6)
val md_light_onPrimaryContainer = Color(0xFF171A1F)

val md_light_secondary = Color(0xFF4F5A67)
val md_light_onSecondary = Color(0xFFFFFFFF)
val md_light_secondaryContainer = Color(0xFFE1E6EB)
val md_light_onSecondaryContainer = Color(0xFF1B222A)

val md_light_tertiary = Color(0xFF686154)
val md_light_onTertiary = Color(0xFFFFFFFF)
val md_light_tertiaryContainer = Color(0xFFEEE8DC)
val md_light_onTertiaryContainer = Color(0xFF26221C)

val md_light_error = Color(0xFF9E4141)
val md_light_onError = Color(0xFFFFFFFF)
val md_light_errorContainer = Color(0xFFFFDADA)
val md_light_onErrorContainer = Color(0xFF3F0709)

val md_light_background = Color(0xFFF4F3F0)
val md_light_onBackground = Color(0xFF161719)
val md_light_surface = Color(0xFFFCFBF8)
val md_light_onSurface = Color(0xFF161719)
val md_light_surfaceVariant = Color(0xFFE5E4E0)
val md_light_onSurfaceVariant = Color(0xFF505156)
val md_light_outline = Color(0xFF77787D)
val md_light_outlineVariant = Color(0xFFC9C8C4)
val md_light_inverseSurface = Color(0xFF2A2B2E)
val md_light_inverseOnSurface = Color(0xFFF2F1ED)
val md_light_inversePrimary = Color(0xFFD6DBE1)
val md_light_scrim = Color(0xFF000000)

val md_light_surfaceContainer = Color(0xFFECEBE7)
val md_light_surfaceContainerHigh = Color(0xFFE2E1DD)

// ---------- Dark: black-forward layered charcoal ----------
val md_dark_primary = Color(0xFFE5E7EA)
val md_dark_onPrimary = Color(0xFF202329)
val md_dark_primaryContainer = Color(0xFF353A42)
val md_dark_onPrimaryContainer = Color(0xFFF2F4F6)

val md_dark_secondary = Color(0xFFBCC4CD)
val md_dark_onSecondary = Color(0xFF252A31)
val md_dark_secondaryContainer = Color(0xFF3C444E)
val md_dark_onSecondaryContainer = Color(0xFFEAF0F5)

val md_dark_tertiary = Color(0xFFD0C7B7)
val md_dark_onTertiary = Color(0xFF302D28)
val md_dark_tertiaryContainer = Color(0xFF48433C)
val md_dark_onTertiaryContainer = Color(0xFFF2ECE2)

val md_dark_error = Color(0xFFFFB4B4)
val md_dark_onError = Color(0xFF5F1114)
val md_dark_errorContainer = Color(0xFF812C2F)
val md_dark_onErrorContainer = Color(0xFFFFDADA)

val md_dark_background = Color(0xFF08090A)
val md_dark_onBackground = Color(0xFFF2F3F4)
val md_dark_surface = Color(0xFF0E1012)
val md_dark_onSurface = Color(0xFFF2F3F4)
val md_dark_surfaceVariant = Color(0xFF1A1D21)
val md_dark_onSurfaceVariant = Color(0xFFB8BDC4)
val md_dark_outline = Color(0xFF8A9098)
val md_dark_outlineVariant = Color(0xFF34383E)
val md_dark_inverseSurface = Color(0xFFE4E5E7)
val md_dark_inverseOnSurface = Color(0xFF24262A)
val md_dark_inversePrimary = Color(0xFF505761)
val md_dark_scrim = Color(0xFF000000)

val md_dark_surfaceContainer = Color(0xFF14171A)
val md_dark_surfaceContainerHigh = Color(0xFF1C2024)

// ---------- Obsidian gradient stops ----------
// Restrained graphite movement keeps white hero content legible without looking flat.
val brandGradientLightStart = Color(0xFF202327)
val brandGradientLightMid = Color(0xFF343A41)
val brandGradientLightEnd = Color(0xFF535C66)

val brandGradientDarkStart = Color(0xFF050607)
val brandGradientDarkMid = Color(0xFF111419)
val brandGradientDarkEnd = Color(0xFF252B33)

val brandStatusGradientDarkStart = Color(0xFF050607)
val brandStatusGradientDarkMid = Color(0xFF0C0E11)
val brandStatusGradientDarkEnd = Color(0xFF171B20)

// Light mode uses a pale smoke status strip so Android's dark system icons remain legible.
val brandStatusGradientLightStart = Color(0xFFE8E9EB)
val brandStatusGradientLightMid = Color(0xFFDDE0E4)
val brandStatusGradientLightEnd = Color(0xFFD2D6DC)

// Warm metal accent used sparingly for rankings, highlights and new markers.
val accentAmber = Color(0xFFAA8C58)
val accentAmberSoft = Color(0xFFE8DFCF)

// ---------- Muted semantic result colors ----------
val gradeOutstanding = Color(0xFF3F8065) // O
val gradeExcellent = Color(0xFF548866)   // A+, A
val gradeGood = Color(0xFF587C98)        // B+, B
val gradeAverage = Color(0xFF9B7B42)     // C
val gradeBorderline = Color(0xFFAA6747)  // D
val gradeFail = Color(0xFFA94F52)        // F, Ab
