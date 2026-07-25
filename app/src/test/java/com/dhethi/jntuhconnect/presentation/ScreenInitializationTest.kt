package com.dhethi.jntuhconnect.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class ScreenInitializationTest {
    @Test
    fun `first screen accessed remains available to route lookup`() {
        assertEquals("updates", Screen.Updates.route)
        assertEquals(Screen.Updates, Screen.fromRoute("updates"))
    }
}
