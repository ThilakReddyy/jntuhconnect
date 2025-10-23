package com.dhethi.jntuhconnect.domain.model


data class AcademicResult(
    val backlogs: Int,
    val cgpa: String,
    val semesters: List<Semester>
)
