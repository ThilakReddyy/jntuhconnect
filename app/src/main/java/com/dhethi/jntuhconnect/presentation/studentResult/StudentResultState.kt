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
    val allLoading: Boolean = false,
    val backlogLoading: Boolean = false,
    val creditsLoading: Boolean = false,
    val allError: String = "",
    val backlogError: String = "",
    val creditsError: String = "",
    val currentTab: String = Constants.ACADEMIC_RESULTS
)
