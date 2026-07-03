package com.dhethi.jntuhconnect.data.remote.dto

import androidx.annotation.Keep
import com.dhethi.jntuhconnect.domain.model.BacklogResult
import com.dhethi.jntuhconnect.domain.model.GraceEligibility
import com.dhethi.jntuhconnect.domain.model.GraceProofResult
import com.google.gson.annotations.SerializedName

/**
 * Grace-marks eligibility response. On success the body IS the backlog shape
 * (`semesters` + `totalBacklogs`). On failure it carries `status` + `message`.
 */
@Keep
data class GraceEligibilityDto(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("semesters") val semesters: List<SemesterDto>? = null,
    @SerializedName("totalBacklogs") val totalBacklogs: Int? = null
)

fun GraceEligibilityDto.toGraceEligibility(): GraceEligibility {
    // Failure payloads always include a message and no semesters.
    if (status != null || semesters == null) {
        return GraceEligibility(
            eligible = false,
            message = message ?: "You are not eligible for grace marks.",
            backlogResult = null
        )
    }
    return GraceEligibility(
        eligible = true,
        message = null,
        backlogResult = BacklogResult(
            semesters = semesters.map { it.toSemester() },
            totalBacklogs = totalBacklogs ?: 0
        )
    )
}

@Keep
data class GraceProofResponseDto(
    @SerializedName("status") val status: String? = null,
    @SerializedName("rollNumber") val rollNumber: String? = null,
    @SerializedName("downloadUrl") val downloadUrl: String? = null,
    @SerializedName("uploadedAt") val uploadedAt: String? = null,
    @SerializedName("message") val message: String? = null
)

fun GraceProofResponseDto.toGraceProofResult(): GraceProofResult = GraceProofResult(
    rollNumber = rollNumber ?: "",
    downloadUrl = downloadUrl ?: "",
    uploadedAt = uploadedAt ?: ""
)
