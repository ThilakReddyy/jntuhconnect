package com.dhethi.jntuhconnect.presentation.studentResult

import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.domain.model.AcademicResult
import com.dhethi.jntuhconnect.domain.model.BacklogResult
import com.dhethi.jntuhconnect.domain.model.Details
import com.dhethi.jntuhconnect.domain.model.SemesterResult

data class StudentResultState(
    val isLoading: Boolean = false,
    val rollNumber: String = "",
    val academicResult: AcademicResult? = null,
    val allResult: List<SemesterResult>? = emptyList(),
    val backlogResult: BacklogResult? = null,
    val studentDetails: Details? = null,
    val error: String = "",
    val currentTab: String = Constants.ACADEMIC_RESULTS
)
