package com.dhethi.jntuhconnect.data.remote.dto

import androidx.annotation.Keep

@Keep
data class JobsResponseDto(
    val page: Int = 1,
    val pageSize: Int = 20,
    val count: Int = 0,
    val hasMore: Boolean = false,
    val jobs: List<JobDto> = emptyList()
)

@Keep
data class JobDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val company: String = "",
    val companyCanonical: String = "",
    val companyType: String = "OTHER",
    val isProductBased: Boolean = false,
    val isFresher: Boolean = true,
    val companyLogo: String? = null,
    val type: String = "FULL_TIME",
    val experience: String? = null,
    val experienceMin: Int? = null,
    val experienceMax: Int? = null,
    val salary: String? = null,
    val tags: List<String> = emptyList(),
    val applicationUrl: String? = null,
    val isRemote: Boolean = false,
    val eligibilityScope: String = "INDIA_ONSITE",
    val postedAt: String? = null,
    val source: String = "",
    val locations: List<String> = emptyList()
) {
    val displayCompany: String
        get() = companyCanonical.ifBlank { company }

    val displayLocation: String
        get() = locations.filter { it.isNotBlank() }.joinToString().ifBlank { "India" }
}
