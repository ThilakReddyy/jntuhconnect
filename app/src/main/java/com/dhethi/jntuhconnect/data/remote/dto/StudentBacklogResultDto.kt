package com.dhethi.jntuhconnect.data.remote.dto

import com.dhethi.jntuhconnect.domain.model.StudentBacklogResult
import com.google.gson.annotations.SerializedName

data class StudentBacklogResultDto(
    @SerializedName("details")
    val details: DetailsDto?,
    @SerializedName("results")
    val results: BacklogResultDto?
)


fun StudentBacklogResultDto.toStudentBacklogResult(): StudentBacklogResult? {
    val responseDetails = details ?: return null
    val responseResults = results ?: return null
    val backlogResult = responseResults.toBacklogResults() ?: return null
    return try {
        StudentBacklogResult(
            details = responseDetails.toDetails(),
            backlogResult = backlogResult
        )
    } catch (_: NullPointerException) {
        null
    }
}
