package com.dhethi.jntuhconnect.data.remote.dto

import androidx.annotation.Keep
import com.dhethi.jntuhconnect.domain.model.SemesterResult

@Keep
data class SemesterResultDto(
    val semester: String,
    val exams: List<ExamResultDto>
)


fun SemesterResultDto.toSemesterResult(): SemesterResult {
    return try {
        SemesterResult(
            semester = semester,

            exams = exams.map { it.toExamResult() }
        )
    } catch (e: Exception) {
        e.printStackTrace() // optional: log
        SemesterResult(
            semester = semester,
            exams = emptyList()
        )
    }


}