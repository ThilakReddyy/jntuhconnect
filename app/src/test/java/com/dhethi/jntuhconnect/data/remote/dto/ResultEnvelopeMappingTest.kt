package com.dhethi.jntuhconnect.data.remote.dto

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ResultEnvelopeMappingTest {
    private val gson = Gson()

    @Test
    fun `backlog response with null details is rejected`() {
        val dto = gson.fromJson(
            """{"details":null,"results":{"totalBacklogs":0,"semesters":[]}}""",
            StudentBacklogResultDto::class.java
        )

        assertNull(dto.toStudentBacklogResult())
    }

    @Test
    fun `backlog response with null results is rejected`() {
        val dto = gson.fromJson(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0479","branch":"CSE"},"results":null}""",
            StudentBacklogResultDto::class.java
        )

        assertNull(dto.toStudentBacklogResult())
    }

    @Test
    fun `backlog response with missing total is rejected`() {
        val dto = gson.fromJson(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0479","branch":"CSE"},"results":{"semesters":[]}}""",
            StudentBacklogResultDto::class.java
        )

        assertNull(dto.toStudentBacklogResult())
    }

    @Test
    fun `backlog response with null total is rejected`() {
        val dto = gson.fromJson(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0479","branch":"CSE"},"results":{"totalBacklogs":null,"semesters":[]}}""",
            StudentBacklogResultDto::class.java
        )

        assertNull(dto.toStudentBacklogResult())
    }

    @Test
    fun `backlog response with malformed nested results is rejected`() {
        val dto = gson.fromJson(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0479","branch":"CSE"},"results":{"totalBacklogs":1,"semesters":null}}""",
            StudentBacklogResultDto::class.java
        )

        assertNull(dto.toStudentBacklogResult())
    }

    @Test
    fun `complete backlog response maps successfully`() {
        val dto = gson.fromJson(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0479","branch":"CSE"},"results":{"totalBacklogs":0,"semesters":[]}}""",
            StudentBacklogResultDto::class.java
        )

        val result = dto.toStudentBacklogResult()
        assertNotNull(result)
        assertEquals("18E51A0479", result?.details?.rollNumber)
        assertEquals(0, result?.backlogResult?.totalBacklogs)
    }

    @Test
    fun `academic response with null results is rejected`() {
        val dto = gson.fromJson(
            """{"details":{"collegeCode":"E5","fatherName":"Parent","name":"Student","rollNumber":"18E51A0473","branch":"CSE"},"results":null}""",
            StudentAcademicResultDto::class.java
        )

        assertNull(dto.toStudentAcademicResult())
    }

    @Test
    fun `all-results response with null details is rejected`() {
        val dto = gson.fromJson(
            """{"details":null,"results":[]}""",
            StudentAllResultDto::class.java
        )

        assertNull(dto.toStudentAllResult())
    }
}
