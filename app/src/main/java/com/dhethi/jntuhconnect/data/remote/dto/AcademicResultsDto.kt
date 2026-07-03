package com.dhethi.jntuhconnect.data.remote.dto


import com.dhethi.jntuhconnect.domain.model.AcademicResult
import com.google.gson.annotations.SerializedName

data class AcademicResultsDto(
    @SerializedName("backlogs")
    val backlogs: Int,
    @SerializedName("CGPA")
    val cGPA: String,
    @SerializedName("credits")
    val credits: Double,
    @SerializedName("grades")
    val grades: Double,
    @SerializedName("semesters")
    val semesters: List<SemesterDto>
)

fun AcademicResultsDto.toResults(): AcademicResult {
    return AcademicResult(
        backlogs = backlogs,
        cgpa = cGPA,
        credits = credits,
        semesters = semesters.map { it.toSemester() },
    )
}