package com.dhethi.jntuhconnect.data.remote.dto

import com.dhethi.jntuhconnect.domain.model.BacklogResult
import com.google.gson.annotations.SerializedName

data class BacklogResultDto(
    @SerializedName("totalBacklogs")
    val totalBacklogs: Int?,

    @SerializedName("semesters")
    val semesters: List<SemesterDto>
)

fun BacklogResultDto.toBacklogResults(): BacklogResult? {
    val backlogCount = totalBacklogs ?: return null
    return try {
        BacklogResult(
            semesters = semesters.map { it.toSemester() },
            totalBacklogs = backlogCount
        )
    } catch (_: NullPointerException) {
        null
    }
}
