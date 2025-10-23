package com.dhethi.jntuhconnect.domain.repository

import com.dhethi.jntuhconnect.data.remote.dto.LatestNotificationDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentAcademicResultDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentAllResultDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentBacklogResultDto

interface JntuhResultsRepository {

    suspend fun getAcademicResult(rollNumber: String): StudentAcademicResultDto

    suspend fun getAcademicAllResult(rollNumber: String): StudentAllResultDto

    suspend fun getAcademicBacklogs(rollNumber: String): StudentBacklogResultDto

    suspend fun getNotifications(page: Int, category: String): List<LatestNotificationDto>
}