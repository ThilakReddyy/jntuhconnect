package com.dhethi.jntuhconnect.presentation.classresult

import com.dhethi.jntuhconnect.domain.model.AcademicResult
import com.dhethi.jntuhconnect.domain.model.ClassStudent
import com.dhethi.jntuhconnect.domain.model.Details
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClassRankingTest {
    @Test
    fun `unsorted API input produces the correct topper and CGPA`() {
        val ranking = rankClassStudents(
            listOf(
                student("18E51A0479", "8.20"),
                student("18E51A0473", "9.60"),
                student("18E51A0475", "9.10")
            )
        )

        assertEquals("18E51A0473", ranking.toppers.single().student.details.rollNumber)
        assertEquals("9.60", ranking.topCgpa?.toPlainString())
        assertEquals(listOf(1, 2, 3), ranking.ranked.map { it.rank })
    }

    @Test
    fun `equal CGPAs share rank and use roll number for stable ordering`() {
        val ranking = rankClassStudents(
            listOf(
                student("18E51A0479", "9.50"),
                student("18E51A0473", "9.5"),
                student("18E51A0475", "9.20")
            )
        )

        assertEquals(listOf("18E51A0473", "18E51A0479", "18E51A0475"), ranking.ranked.map { it.student.details.rollNumber })
        assertEquals(listOf(1, 1, 3), ranking.ranked.map { it.rank })
        assertEquals(2, ranking.toppers.size)
    }

    @Test
    fun `invalid CGPAs and nonzero backlogs are not ranked`() {
        val students = listOf(
            student("NULL", "0", hasResult = false),
            student("BLANK", " "),
            student("NAN", "NaN"),
            student("INFINITY", "Infinity"),
            student("NEGATIVE", "-0.01"),
            student("HIGH", "10.01"),
            student("BACKLOG", "9.90", backlogs = 1),
            student("BAD_BACKLOG", "9.90", backlogs = -1)
        )

        val ranking = rankClassStudents(students)

        assertTrue(ranking.ranked.isEmpty())
        assertEquals(students, ranking.unranked)
        assertEquals(8, ranking.studentCount)
    }

    @Test
    fun `zero and ten are valid CGPA boundaries`() {
        val ranking = rankClassStudents(
            listOf(student("ZERO", "0"), student("TEN", "10"))
        )

        assertEquals(listOf("TEN", "ZERO"), ranking.ranked.map { it.student.details.rollNumber })
        assertEquals("10", ranking.topCgpa?.toPlainString())
        assertEquals(5.0, ranking.averageCgpa ?: -1.0, 0.0)
    }

    private fun student(
        rollNumber: String,
        cgpa: String,
        backlogs: Int = 0,
        hasResult: Boolean = true
    ) = ClassStudent(
        details = Details(
            name = "Student $rollNumber",
            collegeCode = "E5",
            fatherName = "Parent",
            rollNumber = rollNumber,
            branch = "CSE"
        ),
        result = if (hasResult) {
            AcademicResult(
                backlogs = backlogs,
                cgpa = cgpa,
                credits = 0.0,
                semesters = emptyList()
            )
        } else {
            null
        }
    )
}
