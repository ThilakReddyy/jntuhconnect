package com.dhethi.jntuhconnect.domain.model

/**
 * Result of a grace-marks eligibility check. When [eligible] is true, [backlogResult]
 * lists the subjects that could be raised by grace marks. Otherwise [message] explains
 * why the student is not eligible.
 */
data class GraceEligibility(
    val eligible: Boolean,
    val message: String?,
    val backlogResult: BacklogResult?
)

/** Successful proof upload result. */
data class GraceProofResult(
    val rollNumber: String,
    val downloadUrl: String,
    val uploadedAt: String
)
