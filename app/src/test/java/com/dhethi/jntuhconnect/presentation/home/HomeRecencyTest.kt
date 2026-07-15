package com.dhethi.jntuhconnect.presentation.home

import com.dhethi.jntuhconnect.domain.model.LatestNotification
import com.dhethi.jntuhconnect.domain.model.RecentDocument
import java.util.Calendar
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeRecencyTest {

    @Test
    fun notificationsOnlyIncludeTodayAndYesterday() {
        val now = Calendar.getInstance().apply {
            set(2026, Calendar.JULY, 15, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val updates = listOf(
            update("today", "2026-07-15"),
            update("yesterday", "2026-07-14"),
            update("older", "2026-07-13")
        )

        assertEquals(listOf("today", "yesterday"), recentNotifications(updates, now).map { it.title })
    }

    @Test
    fun documentExpiresAtTwentyFourHours() {
        val now = 100_000_000L
        val recent = RecentDocument("Recent", "url", RecentDocument.SYLLABUS, now - 1_000L)
        val expired = RecentDocument(
            "Expired",
            "url",
            RecentDocument.CALENDAR,
            now - RecentDocument.MAX_AGE_MILLIS
        )

        assertTrue(recent.isWithinLast24Hours(now))
        assertFalse(expired.isWithinLast24Hours(now))
    }

    private fun update(title: String, releaseDate: String) = LatestNotification(
        date = releaseDate,
        link = "https://example.com/$title",
        releaseDate = releaseDate,
        title = title,
        category = "all"
    )
}
