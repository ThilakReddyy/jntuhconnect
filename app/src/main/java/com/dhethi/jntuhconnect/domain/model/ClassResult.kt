package com.dhethi.jntuhconnect.domain.model

/** One student in a class-wide academic result list. [result] is null if not yet synced. */
data class ClassStudent(
    val details: Details,
    val result: AcademicResult?
)

/** One student in a class-wide backlog list. */
data class ClassBacklogStudent(
    val details: Details,
    val backlogResult: BacklogResult?
)
