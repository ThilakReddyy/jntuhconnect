package com.dhethi.jntuhconnect.data.remote.dto

import androidx.annotation.Keep
import com.dhethi.jntuhconnect.domain.model.ExamResult


@Keep
data class ExamResultDto(
    val examCode: String,
    val rcrv: Boolean,
    val graceMarks: Boolean,
    val subjects: List<SubjectDto>
)


fun ExamResultDto.toExamResult(): ExamResult {
    return ExamResult(
        examCode = examCode,
        rcrv = rcrv,
        graceMarks = graceMarks,
        subjects = subjects.map { it -> it.toSubject() }
    )
}