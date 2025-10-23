package com.dhethi.jntuhconnect.data.remote.dto


import com.dhethi.jntuhconnect.domain.model.Semester
import com.google.gson.annotations.SerializedName

data class SemesterDto(
    @SerializedName("backlogs")
    val backlogs: Double,
    @SerializedName("failed")
    val failed: Boolean,
    @SerializedName("semester")
    val semester: String,
    @SerializedName("semesterCredits")
    val semesterCredits: Double,
    @SerializedName("semesterGrades")
    val semesterGrades: Double,
    @SerializedName("semesterSGPA")
    val semesterSGPA: String,
    @SerializedName("subjects")
    val subjects: List<SubjectDto>
)


fun SemesterDto.toSemester(): Semester {
    return Semester(
        backlogs = backlogs,
        failed = failed,
        semester = semester,
        semesterCredits = semesterCredits,
        semesterGrades = semesterGrades,
        semesterSGPA = semesterSGPA,
        subjects = subjects.map { it.toSubject() }
    )

}
