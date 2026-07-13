package com.dhethi.jntuhconnect.presentation.updates

import com.dhethi.jntuhconnect.domain.model.LatestNotification
import org.junit.Assert.assertEquals
import org.junit.Test

class UpdatesPaginationTest {
    @Test
    fun mergeUpdates_appendsNewItemsAndRemovesOverlappingPageEntries() {
        val first = notification("First", "one")
        val second = notification("Second", "two")

        val result = mergeUpdates(listOf(first), listOf(first, second), append = true)

        assertEquals(listOf(first, second), result)
    }

    @Test
    fun mergeUpdates_replacesItemsForARefresh() {
        val old = notification("Old", "old")
        val fresh = notification("Fresh", "fresh")

        assertEquals(listOf(fresh), mergeUpdates(listOf(old), listOf(fresh), append = false))
    }

    private fun notification(title: String, link: String) = LatestNotification(
        date = "13-07-2026",
        link = link,
        releaseDate = "2026-07-13",
        title = title,
        category = "Results"
    )
}
