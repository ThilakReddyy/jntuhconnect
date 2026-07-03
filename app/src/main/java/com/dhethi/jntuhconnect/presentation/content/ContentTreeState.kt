package com.dhethi.jntuhconnect.presentation.content

import com.dhethi.jntuhconnect.domain.model.ContentNode

/** UI state for the drill-down content screens (academic calendars, syllabus). */
data class ContentTreeState(
    val isLoading: Boolean = false,
    val error: String = "",
    val root: ContentNode? = null
)
