package com.dhethi.jntuhconnect.data.remote

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationPreferencesRequestTest {
    @Test
    fun `serializes backend field names`() {
        val json = Gson().toJsonTree(
            NotificationPreferencesRequest(
                deviceId = "550e8400-e29b-41d4-a716-446655440000",
                degrees = listOf("btech"),
                regulations = listOf("R18")
            )
        ).asJsonObject

        assertEquals(
            setOf("deviceId", "platform", "degrees", "regulations"),
            json.keySet()
        )
    }

    @Test
    fun `defaults platform to android`() {
        val request = NotificationPreferencesRequest(
            deviceId = "550e8400-e29b-41d4-a716-446655440000",
            degrees = emptyList(),
            regulations = emptyList()
        )

        assertEquals("android", request.platform)
    }
}
