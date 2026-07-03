package com.dhethi.jntuhconnect.presentation.updates

import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.domain.model.LatestNotification

data class UpdatesState(
    val currentTab: String = Constants.ALL_UPDATES,
    val page: Int = 1,
    val updates: List<LatestNotification> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val endReached: Boolean = false,
    // Optional filters
    val degreeLabel: String = "",   // display label, e.g. "B.Tech"
    val degreeCode: String = "",    // backend code, e.g. "btech"
    val regulation: String = "",    // e.g. "R22"
    val year: String = "",          // e.g. "2024"
    val titleQuery: String = ""
) {
    val activeFilterCount: Int
        get() = listOf(degreeCode, regulation, year, titleQuery).count { it.isNotBlank() }
}
