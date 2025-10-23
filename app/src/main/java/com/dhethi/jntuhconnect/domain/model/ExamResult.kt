package com.dhethi.jntuhconnect.domain.model



data class ExamResult(
    val examCode: String,
    val rcrv: Boolean,
    val graceMarks: Boolean,
    val subjects: List<Subject>
)
