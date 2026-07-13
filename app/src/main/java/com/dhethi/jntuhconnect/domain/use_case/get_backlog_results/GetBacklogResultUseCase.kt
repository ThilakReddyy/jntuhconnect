package com.dhethi.jntuhconnect.domain.use_case.get_backlog_results

import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.data.remote.dto.toStudentBacklogResult
import com.dhethi.jntuhconnect.domain.model.StudentBacklogResult
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetBacklogResultUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    operator fun invoke(rollNumber: String): Flow<Resource<StudentBacklogResult>> = flow {
        try {
            emit(Resource.Loading())
            val backlogResult = repository.getAcademicBacklogs(rollNumber).toStudentBacklogResult()
            if (backlogResult == null) {
                emit(Resource.Error("Backlog results are still being processed. Please try again shortly."))
            } else {
                emit(Resource.Success(backlogResult))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "A unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't Reach Server. Check Your Internet Connection"))
        } catch (e: Exception) {
            emit(Resource.Error("We couldn't read the backlog response. Please try again."))
        }
    }
}
