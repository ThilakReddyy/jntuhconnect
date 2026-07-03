package com.dhethi.jntuhconnect.domain.repository

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

interface JntuhResultsRepository {

    suspend fun getAcademicResult(rollNumber: String): StudentAcademicResultDto

    suspend fun getAcademicAllResult(rollNumber: String): StudentAllResultDto

    suspend fun getAcademicBacklogs(rollNumber: String): StudentBacklogResultDto

    suspend fun getCreditsChecker(rollNumber: String): CreditsResponseDto

    suspend fun getResultContrast(rollNumber1: String, rollNumber2: String): ResultContrastDto

    suspend fun getClassAcademicResults(rollNumber: String): List<ClassAcademicStudentDto>

    suspend fun getClassBacklogResults(rollNumber: String): List<ClassBacklogStudentDto>

    suspend fun checkGraceEligibility(rollNumber: String): GraceEligibilityDto

    suspend fun uploadGraceProof(rollNumber: String, file: MultipartBody.Part): GraceProofResponseDto

    suspend fun getNotifications(
        page: Int,
        category: String,
        regulation: String = "",
        degree: String = "",
        year: String = "",
        title: String = ""
    ): List<LatestNotificationDto>

    suspend fun getCalendars(): JsonObject

    suspend fun getSyllabus(): JsonObject
}
