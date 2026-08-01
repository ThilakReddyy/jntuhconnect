package com.dhethi.jntuhconnect.data.remote

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class ResultSubscriptionRequestTest {
    @Test
    fun `serializes backend field names`() {
        val json = Gson().toJsonTree(
            ResultSubscriptionRequest(
                deviceId = "550e8400-e29b-41d4-a716-446655440000",
                deviceToken = "firebase-token",
                rollNumber = "25P81A6602"
            )
        ).asJsonObject

        assertEquals(
            setOf("deviceId", "deviceToken", "rollNumber"),
            json.keySet()
        )
    }
}
