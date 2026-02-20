package com.dhethi.jntuhconnect.domain.repository

import android.content.Context
import android.graphics.Bitmap
import java.io.File

interface PdfRepository {

    suspend fun downloadPdfToCache( pdfUrl: String): File

    suspend fun convertPdfToBitmaps(pdfFile: File): List<Bitmap>
}