package com.dhethi.jntuhconnect.data.remote.dto

import androidx.annotation.Keep
import com.dhethi.jntuhconnect.domain.model.ContrastProfile
import com.dhethi.jntuhconnect.domain.model.ContrastSemester
import com.dhethi.jntuhconnect.domain.model.ResultContrast
import com.google.gson.annotations.SerializedName

@Keep
data class ResultContrastDto(
    @SerializedName("studentProfiles") val studentProfiles: List<ContrastProfileDto>? = null,
    @SerializedName("semesters") val semesters: List<List<ContrastSemesterDto>>? = null,
    // Present when one roll number is missing and a scrape was queued.
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null
)

@Keep
data class ContrastProfileDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("rollNumber") val rollNumber: String? = null,
    @SerializedName("collegeCode") val collegeCode: String? = null,
    @SerializedName("fatherName") val fatherName: String? = null,
    // Either a formatted string ("8.74") or 0.0 when there are backlogs.
    @SerializedName("CGPA") val cgpa: String? = null,
    @SerializedName("backlogs") val backlogs: Int? = null,
    @SerializedName("credits") val credits: Double? = null
)

@Keep
data class ContrastSemesterDto(
    @SerializedName("semester") val semester: String? = null,
    @SerializedName("semesterSGPA") val semesterSGPA: String? = null,
    @SerializedName("semesterCredits") val semesterCredits: String? = null,
    @SerializedName("semesterGrades") val semesterGrades: String? = null,
    @SerializedName("backlogs") val backlogs: String? = null,
    @SerializedName("failed") val failed: Boolean? = null
)

fun ResultContrastDto.toResultContrast(): ResultContrast = ResultContrast(
    studentProfiles = (studentProfiles ?: emptyList()).map {
        ContrastProfile(
            name = it.name ?: "",
            rollNumber = it.rollNumber ?: "",
            collegeCode = it.collegeCode ?: "",
            fatherName = it.fatherName ?: "",
            cgpa = it.cgpa ?: "-",
            backlogs = it.backlogs ?: 0,
            credits = it.credits ?: 0.0
        )
    },
    semesters = (semesters ?: emptyList()).map { pair ->
        pair.map {
            ContrastSemester(
                semester = it.semester ?: "-",
                semesterSGPA = it.semesterSGPA ?: "-",
                semesterCredits = it.semesterCredits ?: "-",
                backlogs = it.backlogs ?: "-",
                failed = it.failed ?: false
            )
        }
    }
)
