package com.dhethi.jntuhconnect.domain.model

/** One academic year's credit breakdown (two semesters + the year's cumulative target). */
data class AcademicYearCredits(
    val semesterWiseCredits: Map<String, Double>,
    val creditsObtained: Double,
    val requiredForYear: Double
)

/** Credits earned vs the regulation's required-credits table. */
data class CreditsResult(
    val academicYears: List<AcademicYearCredits>,
    val totalCredits: Double,
    val totalObtainedCredits: Double,
    val totalRequiredCredits: Double
)

/**
 * Wrapper for the credits screen. [available] is false when the feature does not apply
 * (e.g. non-B.Tech), in which case [message] explains why and [credits] is null.
 */
data class StudentCreditsResult(
    val details: Details?,
    val credits: CreditsResult?,
    val available: Boolean,
    val message: String?
)
