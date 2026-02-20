package com.dhethi.jntuhconnect.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.dhethi.jntuhconnect.domain.repository.PdfRepository
import java.io.File
import java.net.URL
import javax.inject.Inject
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PdfRepositoryImpl @Inject constructor(
    private val context: Context
) : PdfRepository {
    override suspend fun downloadPdfToCache(
        pdfUrl: String
    ): File = withContext(Dispatchers.IO){
        val cacheDir = context.cacheDir
        val pdfFile = File(cacheDir, "temp.pdf")

        URL(pdfUrl).openStream().use { input ->
            pdfFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return@withContext pdfFile
    }

    override suspend fun convertPdfToBitmaps(pdfFile: File): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()
        val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(fileDescriptor)

        for (i in 0 until renderer.pageCount) {
            renderer.openPage(i).use { page ->
                val bitmap = createBitmap(page.width, page.height)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmaps.add(bitmap)
            }
        }

        renderer.close()
        fileDescriptor.close()

        return bitmaps
    }
}