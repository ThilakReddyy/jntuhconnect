package com.dhethi.jntuhconnect.presentation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ScreenTest {
    @Test
    fun `every screen has a unique analytics name`() {
        assertEquals(Screen.all.size, Screen.all.map { it.analyticsName }.distinct().size)
    }

    @Test
    fun `routes with arguments resolve without including user data`() {
        assertEquals(
            Screen.StudentResults,
            Screen.fromRoute("studentResults/{rollNumber}?startTab={startTab}")
        )
        assertEquals(Screen.StudentResults, Screen.fromRoute("studentResults/18E51A0479"))
    }

    @Test
    fun `unknown routes are not reported as an incorrect screen`() {
        assertNull(Screen.fromRoute(null))
        assertNull(Screen.fromRoute("unknown"))
    }
}
