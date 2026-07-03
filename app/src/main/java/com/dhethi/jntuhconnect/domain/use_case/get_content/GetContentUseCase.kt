package com.dhethi.jntuhconnect.domain.use_case.get_content

import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.data.remote.dto.toContentNode
import com.dhethi.jntuhconnect.domain.model.ContentNode
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

/** Loads the academic-calendars / syllabus trees and maps them to a typed [ContentNode]. */
class GetContentUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    fun calendars(): Flow<Resource<ContentNode>> = load { repository.getCalendars().toContentNode() }

    fun syllabus(): Flow<Resource<ContentNode>> = load { repository.getSyllabus().toContentNode() }

    private fun load(block: suspend () -> ContentNode): Flow<Resource<ContentNode>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(block()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}
