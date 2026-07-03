package com.dhethi.jntuhconnect.presentation.home

import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.domain.model.LatestNotification

data class HomeState(
    val students: List<StudentDetailsEntity> = emptyList(),
    val latestUpdates: List<LatestNotification> = emptyList(),
    val updatesLoading: Boolean = false
)
