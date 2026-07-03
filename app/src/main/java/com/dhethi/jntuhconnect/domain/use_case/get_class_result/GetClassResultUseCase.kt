package com.dhethi.jntuhconnect.domain.use_case.get_class_result

import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.common.friendlyMessage
import com.dhethi.jntuhconnect.data.remote.dto.toClassBacklogStudent
import com.dhethi.jntuhconnect.data.remote.dto.toClassStudent
import com.dhethi.jntuhconnect.domain.model.ClassBacklogStudent
import com.dhethi.jntuhconnect.domain.model.ClassStudent
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetClassResultUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    fun academic(rollNumber: String): Flow<Resource<List<ClassStudent>>> = flow {
        try {
            emit(Resource.Loading())
            val students = repository.getClassAcademicResults(rollNumber)
                .mapNotNull { it.toClassStudent() }
            emit(Resource.Success(students))
        } catch (e: HttpException) {
            emit(Resource.Error(e.friendlyMessage()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    fun backlogs(rollNumber: String): Flow<Resource<List<ClassBacklogStudent>>> = flow {
        try {
            emit(Resource.Loading())
            val students = repository.getClassBacklogResults(rollNumber)
                .mapNotNull { it.toClassBacklogStudent() }
            emit(Resource.Success(students))
        } catch (e: HttpException) {
            emit(Resource.Error(e.friendlyMessage()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}
