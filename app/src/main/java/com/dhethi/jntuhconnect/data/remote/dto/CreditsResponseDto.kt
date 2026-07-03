package com.dhethi.jntuhconnect.data.remote.dto

import androidx.annotation.Keep
import com.dhethi.jntuhconnect.domain.model.AcademicYearCredits
import com.dhethi.jntuhconnect.domain.model.CreditsResult
import com.dhethi.jntuhconnect.domain.model.StudentCreditsResult
import com.google.gson.annotations.SerializedName

@Keep
data class CreditsResponseDto(
    @SerializedName("details") val details: DetailsDto? = null,
    @SerializedName("results") val results: CreditsDto? = null,
    // Present only for the "not applicable" (non-B.Tech) failure response.
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null
)

@Keep
data class CreditsDto(
    @SerializedName("academicYears") val academicYears: List<AcademicYearCreditsDto>? = null,
    @SerializedName("totalCredits") val totalCredits: Double? = null,
    @SerializedName("totalObtainedCredits") val totalObtainedCredits: Double? = null,
    @SerializedName("totalRequiredCredits") val totalRequiredCredits: Double? = null
)

@Keep
data class AcademicYearCreditsDto(
    @SerializedName("semesterWiseCredits") val semesterWiseCredits: Map<String, Double>? = null,
    @SerializedName("creditsObtained") val creditsObtained: Double? = null,
    @SerializedName("totalCredits") val totalCredits: Double? = null
)

fun CreditsResponseDto.toStudentCreditsResult(): StudentCreditsResult {
    // Non-B.Tech / unsupported regulation → backend returns {status:"Failure", message:...}
    if (status.equals("Failure", ignoreCase = true)) {
        return StudentCreditsResult(
            details = null,
            credits = null,
            available = false,
            message = message ?: "Credits checker is only available for B.Tech students."
        )
    }
    val res = results
    if (res?.academicYears == null) {
        // Pending/queued or empty — treat as unavailable for now.
        return StudentCreditsResult(details = details?.toDetails(), credits = null, available = false, message = null)
    }
    return StudentCreditsResult(
        details = details?.toDetails(),
        available = true,
        message = null,
        credits = CreditsResult(
            academicYears = res.academicYears.map {
                AcademicYearCredits(
                    semesterWiseCredits = it.semesterWiseCredits ?: emptyMap(),
                    creditsObtained = it.creditsObtained ?: 0.0,
                    requiredForYear = it.totalCredits ?: 0.0
                )
            },
            totalCredits = res.totalCredits ?: 0.0,
            totalObtainedCredits = res.totalObtainedCredits ?: 0.0,
            totalRequiredCredits = res.totalRequiredCredits ?: 0.0
        )
    )
}
