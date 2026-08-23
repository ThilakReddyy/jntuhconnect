package com.dhethi.jntuhconnect.presentation.notificationpreferences

data class NotificationPreferencesState(
    val notificationsEnabled: Boolean = false,
    val updatingEnabled: Boolean = false,
    val degrees: Set<String> = emptySet(),   // backend codes, e.g. "btech"
    val regulations: Set<String> = emptySet(), // e.g. "R18"
    val loading: Boolean = true,
    val saving: Boolean = false,
    val error: String? = null
)
