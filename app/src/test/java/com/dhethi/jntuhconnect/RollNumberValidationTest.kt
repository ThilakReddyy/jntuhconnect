package com.dhethi.jntuhconnect

import com.dhethi.jntuhconnect.presentation.components.isValidRollNumber
import com.dhethi.jntuhconnect.presentation.components.normalizeRollNumber
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RollNumberValidationTest {
    @Test
    fun normalizeRollNumber_removesSeparatorsUppercasesAndCapsLength() {
        assertEquals("20J21A0101", normalizeRollNumber(" 20j21-a0101 extra"))
    }

    @Test
    fun isValidRollNumber_acceptsTenAlphanumericCharacters() {
        assertTrue(isValidRollNumber("20J21A0101"))
    }

    @Test
    fun isValidRollNumber_rejectsIncompleteOrPunctuatedValues() {
        assertFalse(isValidRollNumber("20J21A01"))
        assertFalse(isValidRollNumber("20J21A01-1"))
    }
}
