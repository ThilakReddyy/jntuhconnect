package com.dhethi.jntuhconnect.data.remote.dto

import androidx.annotation.Keep
import com.dhethi.jntuhconnect.domain.model.StudentAllResult
import com.google.gson.annotations.SerializedName


@Keep
data class StudentAllResultDto(
    @SerializedName("details")
    val details: DetailsDto?,
    @SerializedName("results")
    val results: List<SemesterResultDto>?
)


fun StudentAllResultDto.toStudentAllResult(): StudentAllResult? {
    val responseDetails = details ?: return null
    val responseResults = results ?: return null
    return try {
        StudentAllResult(
            details = responseDetails.toDetails(),
            results = responseResults.map { it.toSemesterResult() }
        )
    } catch (_: NullPointerException) {
        null
    }


}
