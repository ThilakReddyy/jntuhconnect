package com.dhethi.jntuhconnect.domain.model


data class Semester(
    val backlogs: Double,
    val failed: Boolean,
    val semester: String,
    val semesterCredits: Double,
    val semesterGrades: Double,
    val semesterSGPA: String,
    val subjects: List<Subject>
)
