package com.dhethi.jntuhconnect.domain.use_case.get_contrast

import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.common.friendlyMessage
import com.dhethi.jntuhconnect.data.remote.dto.toResultContrast
import com.dhethi.jntuhconnect.domain.model.ResultContrast
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetResultContrastUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    operator fun invoke(
        rollNumber1: String,
        rollNumber2: String
    ): Flow<Resource<ResultContrast>> = flow {
        try {
            emit(Resource.Loading())
            val dto = repository.getResultContrast(rollNumber1, rollNumber2)
            if (dto.studentProfiles == null) {
                // A missing roll number triggered a queued scrape.
                emit(
                    Resource.Error(
                        dto.message
                            ?: "One or both roll numbers are being fetched. Please try again shortly."
                    )
                )
            } else {
                emit(Resource.Success(dto.toResultContrast()))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.friendlyMessage()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}
