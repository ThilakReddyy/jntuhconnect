package com.dhethi.jntuhconnect.data.repository

import com.dhethi.jntuhconnect.data.remote.JntuhConnectApi
import com.dhethi.jntuhconnect.data.remote.dto.LatestNotificationDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentAcademicResultDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentAllResultDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentBacklogResultDto
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import javax.inject.Inject

class JntuhResultsRepositoryImpl @Inject constructor(
    private val api: JntuhConnectApi
) : JntuhResultsRepository {
    override suspend fun getAcademicResult(rollNumber: String): StudentAcademicResultDto {
        return api.getAcademicResult(rollNumber)
    }

    override suspend fun getAcademicAllResult(rollNumber: String): StudentAllResultDto {
        return api.getAllResults(rollNumber)
    }

    override suspend fun getAcademicBacklogs(rollNumber: String): StudentBacklogResultDto {
        return api.getBacklogs(rollNumber)
    }

    override suspend fun getNotifications(page: Int,category:String):  List<LatestNotificationDto> {
        return api.getLatestNotifications(page,category)
    }
}
