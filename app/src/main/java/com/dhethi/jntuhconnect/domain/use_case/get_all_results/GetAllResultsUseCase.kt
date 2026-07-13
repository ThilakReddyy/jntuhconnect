package com.dhethi.jntuhconnect.domain.use_case.get_all_results

import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.data.remote.dto.toStudentAllResult
import com.dhethi.jntuhconnect.domain.model.StudentAllResult
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetAllResultsUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    operator fun invoke(rollNumber: String): Flow<Resource<StudentAllResult>> = flow {
        try {
            emit(Resource.Loading())
            val allResult = repository.getAcademicAllResult(rollNumber).toStudentAllResult()
            if (allResult == null) {
                emit(Resource.Error("Semester results are incomplete. Please try again shortly."))
            } else {
                emit(Resource.Success(allResult))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "A unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't Reach Server. Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error("We couldn't read the semester response. Please try again."))
        }

    }
}
