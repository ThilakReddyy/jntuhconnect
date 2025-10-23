package com.dhethi.jntuhconnect.data.remote.dto

import com.dhethi.jntuhconnect.domain.model.StudentBacklogResult
import com.google.gson.annotations.SerializedName

data class StudentBacklogResultDto(
    @SerializedName("details")
    val details: DetailsDto,
    @SerializedName("results")
    val results: BacklogResultDto
)


fun StudentBacklogResultDto.toStudentBacklogResult(): StudentBacklogResult {
    return StudentBacklogResult(
        details = details.toDetails(),
        backlogResult = results.toBacklogResults()
    )
}