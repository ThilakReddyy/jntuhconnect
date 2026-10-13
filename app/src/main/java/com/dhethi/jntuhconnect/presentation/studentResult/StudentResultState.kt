package com.dhethi.jntuhconnect.presentation.studentResult

import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.domain.model.AcademicResult
import com.dhethi.jntuhconnect.domain.model.BacklogResult
import com.dhethi.jntuhconnect.domain.model.Details
import com.dhethi.jntuhconnect.domain.model.SemesterResult
import com.dhethi.jntuhconnect.domain.model.StudentCreditsResult

data class StudentResultState(
    val rollNumber: String = "",
    val isLoading: Boolean = false,           // initial academic + hero load
    val error: String = "",
    val studentDetails: Details? = null,
    val academicResult: AcademicResult? = null,
    val allResult: List<SemesterResult>? = null,
    val backlogResult: BacklogResult? = null,
    val creditsResult: StudentCreditsResult? = null,
    val academicLoaded: Boolean = false,
    val allLoaded: Boolean = false,
    val backlogLoaded: Boolean = false,
    val creditsLoaded: Boolean = false,
    val allLoading: Boolean = false,
    val backlogLoading: Boolean = false,
    val creditsLoading: Boolean = false,
    val allError: String = "",
    val backlogError: String = "",
    val creditsError: String = "",
    val currentTab: String = Constants.ACADEMIC_RESULTS
) {
    val selectedResultsReady: Boolean
        get() = studentDetails != null && when (currentTab) {
            Constants.ACADEMIC_RESULTS -> academicLoaded && !isLoading && error.isEmpty() &&
                !academicResult?.semesters.isNullOrEmpty()
            Constants.ALL_RESULTS -> allLoaded && !allLoading && allError.isEmpty() && !allResult.isNullOrEmpty()
            Constants.BACKLOG_RESULTS -> backlogLoaded && !backlogLoading && backlogError.isEmpty() && backlogResult != null
            Constants.CREDIT_RESULTS -> creditsLoaded && !creditsLoading && creditsError.isEmpty() &&
                creditsResult?.available == true && creditsResult.credits != null
            else -> false
        }
}
