package com.dhethi.jntuhconnect.presentation.home

import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.domain.model.LatestNotification
import com.dhethi.jntuhconnect.domain.model.RecentDocument

data class HomeState(
    val students: List<StudentDetailsEntity> = emptyList(),
    val recentDocuments: List<RecentDocument> = emptyList(),
    val latestUpdates: List<LatestNotification> = emptyList(),
    val updatesLoading: Boolean = false
)
