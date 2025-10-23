package com.dhethi.jntuhconnect.data.remote

import com.dhethi.jntuhconnect.data.remote.dto.LatestNotificationDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentAcademicResultDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentAllResultDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentBacklogResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface JntuhConnectApi {

    @GET("getAllResult")
    suspend fun getAllResults(
        @Query("rollNumber") rollNumber: String
    ): StudentAllResultDto

    @GET("getAcademicResult")
    suspend fun getAcademicResult(
        @Query("rollNumber") rollNumber: String
    ): StudentAcademicResultDto

    @GET("getBacklogs")
    suspend fun getBacklogs(
        @Query("rollNumber") rollNumber: String
    ): StudentBacklogResultDto

    @GET("notifications")
    suspend fun getLatestNotifications(
        @Query("page") page: Int,
        @Query("category") category: String
    ): List<LatestNotificationDto>

}