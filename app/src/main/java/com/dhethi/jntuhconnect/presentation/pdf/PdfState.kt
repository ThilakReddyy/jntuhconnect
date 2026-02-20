package com.dhethi.jntuhconnect.presentation.pdf

import android.graphics.Bitmap

data class PdfState(
    val isLoading: Boolean = false,
    val bitmaps: List<Bitmap> = emptyList(),
    val error: String? = null
)