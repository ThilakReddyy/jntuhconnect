package com.dhethi.jntuhconnect.domain.use_case.get_credits

import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.common.friendlyMessage
import com.dhethi.jntuhconnect.data.remote.dto.toStudentCreditsResult
import com.dhethi.jntuhconnect.domain.model.StudentCreditsResult
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCreditsUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    operator fun invoke(rollNumber: String): Flow<Resource<StudentCreditsResult>> = flow {
        try {
            emit(Resource.Loading())
            val result = repository.getCreditsChecker(rollNumber).toStudentCreditsResult()
            // Not available + no message → data is still being fetched (queued).
            if (!result.available && result.message == null && result.credits == null) {
                emit(Resource.Error("Your roll number has been queued. Please try again in a moment."))
            } else {
                emit(Resource.Success(result))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.friendlyMessage()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}
