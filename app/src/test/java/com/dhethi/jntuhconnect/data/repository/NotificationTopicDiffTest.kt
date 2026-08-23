package com.dhethi.jntuhconnect.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationTopicDiffTest {

    @Test
    fun `subscribing from empty to a scoped preference subscribes to every scoped topic`() {
        val current = emptySet<String>()
        val desired = setOf("result-updates-btech", "result-updates-r18", "result-updates-btech-r18")

        assertEquals(desired, topicsToSubscribe(current, desired))
        assertEquals(emptySet<String>(), topicsToUnsubscribe(current, desired))
    }

    @Test
    fun `resetting from a scoped preference to global unsubscribes the scoped topics and subscribes to global`() {
        val current = setOf("result-updates-btech", "result-updates-r18", "result-updates-btech-r18")
        val desired = setOf("result-updates")

        assertEquals(setOf("result-updates"), topicsToSubscribe(current, desired))
        assertEquals(current, topicsToUnsubscribe(current, desired))
    }

    @Test
    fun `switching from the default global topic to a scoped preference unsubscribes result-updates itself`() {
        val current = setOf("result-updates")
        val desired = setOf("result-updates-mtech", "result-updates-r22", "result-updates-mtech-r22")

        assertEquals(desired, topicsToSubscribe(current, desired))
        assertEquals(setOf("result-updates"), topicsToUnsubscribe(current, desired))
    }

    @Test
    fun `switching between two different scoped preferences only touches the changed topics`() {
        val current = setOf("result-updates-btech", "result-updates-r18", "result-updates-btech-r18")
        val desired = setOf("result-updates-btech", "result-updates-r22", "result-updates-btech-r22")

        assertEquals(setOf("result-updates-r22", "result-updates-btech-r22"), topicsToSubscribe(current, desired))
        assertEquals(setOf("result-updates-r18", "result-updates-btech-r18"), topicsToUnsubscribe(current, desired))
    }

    @Test
    fun `no-op when the desired set already matches current`() {
        val current = setOf("result-updates-btech")

        assertEquals(emptySet<String>(), topicsToSubscribe(current, current))
        assertEquals(emptySet<String>(), topicsToUnsubscribe(current, current))
    }
}
