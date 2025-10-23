package com.dhethi.jntuhconnect.presentation.results

import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity

data class ResultsState(
    val rollNumber: String = "",
    val students: List<StudentDetailsEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)