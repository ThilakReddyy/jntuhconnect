package com.dhethi.jntuhconnect.domain.model

/** A single student's summary used in a side-by-side comparison. */
data class ContrastProfile(
    val name: String,
    val rollNumber: String,
    val collegeCode: String,
    val fatherName: String,
    val cgpa: String,
    val backlogs: Int,
    val credits: Double
)

/**
 * One semester row for one student in a comparison. Fields are strings because the
 * backend fills missing semesters with "-" placeholders.
 */
data class ContrastSemester(
    val semester: String,
    val semesterSGPA: String,
    val semesterCredits: String,
    val backlogs: String,
    val failed: Boolean
)

/**
 * Side-by-side comparison of exactly two students. [semesters] is a list of pairs —
 * each inner list has two entries: [student1Row, student2Row] for that semester.
 */
data class ResultContrast(
    val studentProfiles: List<ContrastProfile>,
    val semesters: List<List<ContrastSemester>>
)
