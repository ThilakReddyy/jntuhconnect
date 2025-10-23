package com.dhethi.jntuhconnect.data.remote.dto

import com.dhethi.jntuhconnect.domain.model.BacklogResult
import com.google.gson.annotations.SerializedName

data class BacklogResultDto(
    @SerializedName("totalBacklogs")
    val totalBacklogs: Int,

    @SerializedName("semesters")
    val semesters: List<SemesterDto>
)

fun BacklogResultDto.toBacklogResults(): BacklogResult {
    return BacklogResult(
        semesters = semesters.map { it.toSemester() },
        totalBacklogs = totalBacklogs
    )
}