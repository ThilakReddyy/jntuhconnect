package com.dhethi.jntuhconnect.domain.use_case.load_pdf

import android.content.Context
import android.graphics.Bitmap
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.domain.repository.PdfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class LoadPdfUseCase @Inject constructor(private val repository: PdfRepository) {

    operator fun invoke( url: String): Flow<Resource<List<Bitmap>>> = flow {
        try {
            emit(Resource.Loading())
            val file = repository.downloadPdfToCache( url)
            val bitmaps = repository.convertPdfToBitmaps(file)
            emit(Resource.Success(bitmaps))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "A unexpected error occured"))
        } catch (e: Exception) {
            println(e)
            emit(Resource.Error("Couldn't Reach Server. Check Your Internet Connection"))
        }
    }
}