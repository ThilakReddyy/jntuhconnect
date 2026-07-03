package com.dhethi.jntuhconnect.data.repository

import com.dhethi.jntuhconnect.data.remote.JntuhConnectApi
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
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import javax.inject.Inject

class JntuhResultsRepositoryImpl @Inject constructor(
    private val api: JntuhConnectApi
) : JntuhResultsRepository {

    override suspend fun getAcademicResult(rollNumber: String): StudentAcademicResultDto =
        api.getAcademicResult(rollNumber)

    override suspend fun getAcademicAllResult(rollNumber: String): StudentAllResultDto =
        api.getAllResults(rollNumber)

    override suspend fun getAcademicBacklogs(rollNumber: String): StudentBacklogResultDto =
        api.getBacklogs(rollNumber)

    override suspend fun getCreditsChecker(rollNumber: String): CreditsResponseDto =
        api.getCreditsChecker(rollNumber)

    override suspend fun getResultContrast(
        rollNumber1: String,
        rollNumber2: String
    ): ResultContrastDto = api.getResultContrast(rollNumber1, rollNumber2)

    override suspend fun getClassAcademicResults(rollNumber: String): List<ClassAcademicStudentDto> =
        api.getClassAcademicResults(rollNumber)

    override suspend fun getClassBacklogResults(rollNumber: String): List<ClassBacklogStudentDto> =
        api.getClassBacklogResults(rollNumber)

    override suspend fun checkGraceEligibility(rollNumber: String): GraceEligibilityDto =
        api.checkGraceEligibility(rollNumber)

    override suspend fun uploadGraceProof(
        rollNumber: String,
        file: MultipartBody.Part
    ): GraceProofResponseDto = api.uploadGraceProof(rollNumber, file)

    override suspend fun getNotifications(
        page: Int,
        category: String,
        regulation: String,
        degree: String,
        year: String,
        title: String
    ): List<LatestNotificationDto> =
        api.getLatestNotifications(page, category, regulation, degree, year, title)

    override suspend fun getCalendars(): JsonObject = api.getCalendars()

    override suspend fun getSyllabus(): JsonObject = api.getSyllabus()
}
