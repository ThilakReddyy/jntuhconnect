package com.dhethi.jntuhconnect

import com.dhethi.jntuhconnect.presentation.components.isSafeWebUrl
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WebUrlValidationTest {
    @Test
    fun acceptsHttpAndHttpsUrlsWithHosts() {
        assertTrue(isSafeWebUrl("https://jntuh.ac.in/results"))
        assertTrue(isSafeWebUrl("http://results.jntuh.ac.in"))
    }

    @Test
    fun rejectsEmptyMalformedAndNonWebUrls() {
        assertFalse(isSafeWebUrl(""))
        assertFalse(isSafeWebUrl("not a url"))
        assertFalse(isSafeWebUrl("javascript:alert(1)"))
        assertFalse(isSafeWebUrl("file:///data/local/file.pdf"))
    }
}
