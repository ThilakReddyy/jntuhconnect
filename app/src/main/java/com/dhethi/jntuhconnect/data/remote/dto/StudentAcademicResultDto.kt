package com.dhethi.jntuhconnect.data.remote.dto


import com.dhethi.jntuhconnect.domain.model.StudentAcademicResult
import com.google.gson.annotations.SerializedName

data class StudentAcademicResultDto(
    @SerializedName("details")
    val details: DetailsDto?,
    @SerializedName("results")
    val results: AcademicResultsDto
)

fun StudentAcademicResultDto.toStudentAcademicResult(): StudentAcademicResult {
    return StudentAcademicResult(
        details = details?.toDetails(),
        academicResult = results.toResults()
    )
}