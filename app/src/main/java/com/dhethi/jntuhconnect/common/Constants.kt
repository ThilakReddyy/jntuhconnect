package com.dhethi.jntuhconnect.common

object Constants {
    const val BASE_URL = "https://jntuhresults.dhethi.com/api/"
    const val TITLE = "Jntuh Connect"
    const val PARAM_ROLL_NUMBER = "rollNumber"
    const val ALL_RESULTS = "All Results"
    const val ACADEMIC_RESULTS = "Academic"
    const val BACKLOG_RESULTS = "Backlogs"
    const val CREDIT_RESULTS = "Credits"
    const val ALL_UPDATES = "All"
    const val RESULTS_UPDATES = "Results"
    const val EXAM_UPDATES = "Exams"
    val STUDENT_RESULT_TABS =
        listOf(ALL_RESULTS, ACADEMIC_RESULTS, BACKLOG_RESULTS, CREDIT_RESULTS)
    val UPDATES_TABS =
        listOf(ALL_UPDATES, RESULTS_UPDATES, EXAM_UPDATES)

}