package com.dhethi.jntuhconnect.data.remote.dto


import androidx.annotation.Keep
import com.dhethi.jntuhconnect.domain.model.Subject
import com.google.gson.annotations.SerializedName

@Keep
data class SubjectDto(
    @SerializedName("credits")
    val credits: Double,
    @SerializedName("externalMarks")
    val externalMarks: Int,
    @SerializedName("grades")
    val grades: String,
    @SerializedName("internalMarks")
    val internalMarks: Int,
    @SerializedName("subjectCode")
    val subjectCode: String,
    @SerializedName("subjectName")
    val subjectName: String,
    @SerializedName("totalMarks")
    val totalMarks: Int
)

fun SubjectDto.toSubject(): Subject {
    return Subject(
        credits = credits,
        externalMarks = externalMarks,
        grades = grades,
        internalMarks = internalMarks,
        subjectCode = subjectCode,
        subjectName = subjectName,
        totalMarks = totalMarks
    )
}