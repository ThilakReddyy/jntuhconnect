package com.dhethi.jntuhconnect.data.remote

import com.dhethi.jntuhconnect.data.remote.dto.ClassAcademicStudentDto
import com.dhethi.jntuhconnect.data.remote.dto.ClassBacklogStudentDto
import com.dhethi.jntuhconnect.data.remote.dto.CreditsResponseDto
import com.dhethi.jntuhconnect.data.remote.dto.GraceEligibilityDto
import com.dhethi.jntuhconnect.data.remote.dto.GraceProofResponseDto
import com.dhethi.jntuhconnect.data.remote.dto.LatestNotificationDto
import com.dhethi.jntuhconnect.data.remote.dto.ResultContrastDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentAcademicResultDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentAllResultDto
import com.dhethi.jntuhconnect.data.remote.dto.StudentBacklogResultDto
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("getCreditsChecker")
    suspend fun getCreditsChecker(
        @Query("rollNumber") rollNumber: String
    ): CreditsResponseDto

    @GET("getResultContrast")
    suspend fun getResultContrast(
        @Query("rollNumber1") rollNumber1: String,
        @Query("rollNumber2") rollNumber2: String
    ): ResultContrastDto

    @GET("getClassResults")
    suspend fun getClassAcademicResults(
        @Query("rollNumber") rollNumber: String,
        @Query("type") type: String = "academicresult"
    ): List<ClassAcademicStudentDto>

    @GET("getClassResults")
    suspend fun getClassBacklogResults(
        @Query("rollNumber") rollNumber: String,
        @Query("type") type: String = "backlog"
    ): List<ClassBacklogStudentDto>

    @GET("grace-marks/eligibility")
    suspend fun checkGraceEligibility(
        @Query("rollNumber") rollNumber: String
    ): GraceEligibilityDto

    @Multipart
    @POST("grace-marks/proof")
    suspend fun uploadGraceProof(
        @Query("rollNumber") rollNumber: String,
        @Part file: MultipartBody.Part
    ): GraceProofResponseDto

    @GET("notifications")
    suspend fun getLatestNotifications(
        @Query("page") page: Int,
        @Query("category") category: String,
        @Query("regulation") regulation: String = "",
        @Query("degree") degree: String = "",
        @Query("year") year: String = "",
        @Query("title") title: String = ""
    ): List<LatestNotificationDto>

    /** Nested tree: academic year → degree → study year → { calendar title: PDF link }. */
    @GET("calendars")
    suspend fun getCalendars(): JsonObject

    /** Nested tree: degree → regulation → category → [ { title, link } ]. */
    @GET("syllabus")
    suspend fun getSyllabus(): JsonObject
}
