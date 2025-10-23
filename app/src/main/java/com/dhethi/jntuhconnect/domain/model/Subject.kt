package com.dhethi.jntuhconnect.domain.model



data class Subject(
    val credits: Double,
    val externalMarks: Int,
    val grades: String,
    val internalMarks: Int,
    val subjectCode: String,
    val subjectName: String,
    val totalMarks: Int
)
