package com.dhethi.jntuhconnect.presentation.updates

import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.domain.model.LatestNotification

data class UpdatesState(
    val currentTab: String = Constants.ALL_UPDATES,
    val page: Int = 1,
    val allUpdates: List<LatestNotification> = emptyList(),
    val currentTabUpdates: List<LatestNotification> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val endReached: Boolean = false
)