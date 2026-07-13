package com.dhethi.jntuhconnect.domain.use_case.get_academic_result

import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.data.remote.dto.toStudentAcademicResult
import com.dhethi.jntuhconnect.domain.model.StudentAcademicResult
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetAcademicResultUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    operator fun invoke(rollNumber: String): Flow<Resource<StudentAcademicResult>> = flow {
        try {
            emit(Resource.Loading())
            val dto = repository.getAcademicResult(rollNumber)
            if (dto.details == null) {
                emit(Resource.Error("Your Roll Number has been Queued. Please Try again Later"))
            } else {
                val academicResult = dto.toStudentAcademicResult()
                if (academicResult == null) {
                    emit(Resource.Error("Academic results are incomplete. Please try again shortly."))
                } else {
                    emit(Resource.Success(academicResult))
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "A unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't Reach Server. Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error("We couldn't read the academic response. Please try again."))
        }
    }
}
