package com.dhethi.jntuhconnect.data.remote.dto

import androidx.annotation.Keep
import com.dhethi.jntuhconnect.domain.model.StudentAllResult
import com.google.gson.annotations.SerializedName


@Keep
data class StudentAllResultDto(
    @SerializedName("details")
    val details: DetailsDto,
    @SerializedName("results")
    val results: List<SemesterResultDto>
)


fun StudentAllResultDto.toStudentAllResult(): StudentAllResult {
    return StudentAllResult(
        details = details.toDetails(),
        results = results.map { it.toSemesterResult() }
    )


}