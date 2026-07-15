package com.dhethi.jntuhconnect.domain.model

/** A calendar or syllabus PDF opened by the user within the last 24 hours. */
data class RecentDocument(
    val title: String,
    val link: String,
    val type: String,
    val openedAt: Long
) {
    fun isWithinLast24Hours(now: Long = System.currentTimeMillis()): Boolean =
        openedAt <= now && now - openedAt < MAX_AGE_MILLIS

    companion object {
        const val CALENDAR = "calendar"
        const val SYLLABUS = "syllabus"
        const val MAX_AGE_MILLIS = 24L * 60L * 60L * 1000L
    }
}
