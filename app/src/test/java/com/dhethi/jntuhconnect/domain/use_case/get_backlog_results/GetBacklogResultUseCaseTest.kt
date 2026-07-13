package com.dhethi.jntuhconnect.domain.use_case.get_backlog_results

import com.dhethi.jntuhconnect.common.Resource
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
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class GetBacklogResultUseCaseTest {
    private val gson = Gson()

    @Test
    fun `null details emit loading then processing error`() = runBlocking {
        val states = statesFor(
            """{"details":null,"results":{"totalBacklogs":0,"semesters":[]}}"""
        )

        assertLoadingThenError(states, "Backlog results are still being processed. Please try again shortly.")
    }

    @Test
    fun `null results emit loading then processing error`() = runBlocking {
        val states = statesFor(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0479","branch":"CSE"},"results":null}"""
        )

        assertLoadingThenError(states, "Backlog results are still being processed. Please try again shortly.")
    }

    @Test
    fun `malformed nested results emit loading then processing error`() = runBlocking {
        val states = statesFor(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0479","branch":"CSE"},"results":{"totalBacklogs":1,"semesters":null}}"""
        )

        assertLoadingThenError(states, "Backlog results are still being processed. Please try again shortly.")
    }

    @Test
    fun `missing backlog total emits loading then processing error`() = runBlocking {
        val states = statesFor(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0479","branch":"CSE"},"results":{"semesters":[]}}"""
        )

        assertLoadingThenError(states, "Backlog results are still being processed. Please try again shortly.")
    }

    @Test
    fun `null backlog total emits loading then processing error`() = runBlocking {
        val states = statesFor(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0479","branch":"CSE"},"results":{"totalBacklogs":null,"semesters":[]}}"""
        )

        assertLoadingThenError(states, "Backlog results are still being processed. Please try again shortly.")
    }

    @Test
    fun `valid empty backlog response emits success`() = runBlocking {
        val states = statesFor(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0473","branch":"CSE"},"results":{"totalBacklogs":0,"semesters":[]}}"""
        )

        assertEquals(2, states.size)
        assertTrue(states[0] is Resource.Loading)
        assertTrue(states[1] is Resource.Success)
        assertEquals(0, states[1].data?.backlogResult?.totalBacklogs)
    }

    @Test
    fun `repository cancellation is rethrown`() = runBlocking {
        val useCase = GetBacklogResultUseCase(FakeRepository {
            throw CancellationException("request replaced")
        })

        try {
            useCase("18E51A0479").toList()
            fail("CancellationException should be rethrown")
        } catch (error: CancellationException) {
            assertEquals("request replaced", error.message)
        }
    }

    private suspend fun statesFor(json: String) = GetBacklogResultUseCase(
        FakeRepository {
            gson.fromJson(json, StudentBacklogResultDto::class.java)
        }
    ).invoke("18E51A0479").toList()

    private fun assertLoadingThenError(
        states: List<Resource<com.dhethi.jntuhconnect.domain.model.StudentBacklogResult>>,
        message: String
    ) {
        assertEquals(2, states.size)
        assertTrue(states[0] is Resource.Loading)
        assertTrue(states[1] is Resource.Error)
        assertEquals(message, states[1].message)
    }

    private class FakeRepository(
        private val backlogResponse: suspend () -> StudentBacklogResultDto
    ) : JntuhResultsRepository {
        override suspend fun getAcademicBacklogs(rollNumber: String) = backlogResponse()

        override suspend fun getAcademicResult(rollNumber: String): StudentAcademicResultDto = unused()
        override suspend fun getAcademicAllResult(rollNumber: String): StudentAllResultDto = unused()
        override suspend fun getCreditsChecker(rollNumber: String): CreditsResponseDto = unused()
        override suspend fun getResultContrast(rollNumber1: String, rollNumber2: String): ResultContrastDto = unused()
        override suspend fun getClassAcademicResults(rollNumber: String): List<ClassAcademicStudentDto> = unused()
        override suspend fun getClassBacklogResults(rollNumber: String): List<ClassBacklogStudentDto> = unused()
        override suspend fun checkGraceEligibility(rollNumber: String): GraceEligibilityDto = unused()
        override suspend fun uploadGraceProof(rollNumber: String, file: MultipartBody.Part): GraceProofResponseDto = unused()
        override suspend fun getNotifications(
            page: Int,
            category: String,
            regulation: String,
            degree: String,
            year: String,
            title: String
        ): List<LatestNotificationDto> = unused()

        override suspend fun getCalendars(): JsonObject = unused()
        override suspend fun getSyllabus(): JsonObject = unused()

        private fun <T> unused(): T = error("Not used by this test")
    }
}
