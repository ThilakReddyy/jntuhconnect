package com.dhethi.jntuhconnect.domain.model



data class SemesterResult(
    val semester: String,
    val exams: List<ExamResult>
)
