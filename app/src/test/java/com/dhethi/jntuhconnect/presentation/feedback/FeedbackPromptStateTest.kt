package com.dhethi.jntuhconnect.presentation.feedback

import com.dhethi.jntuhconnect.domain.model.FeedbackDestination
import com.dhethi.jntuhconnect.domain.model.FeedbackPromptState
import com.dhethi.jntuhconnect.domain.model.feedbackDestination
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FeedbackPromptStateTest {
    private val day = 24L * 60 * 60 * 1_000
    private val start = 1_700_000_000_000L

    @Test
    fun `first prompt waits five full days`() {
        val state = FeedbackPromptState(startedAt = start)
        assertFalse(state.isDue(start))
        assertFalse(state.isDue(start + 5 * day - 1))
        assertTrue(state.isDue(start + 5 * day))
    }

    @Test
    fun `dismissal postpones next prompt by another five days`() {
        val dismissed = start + 7 * day
        val state = FeedbackPromptState(startedAt = start, lastPromptAt = dismissed)
        assertFalse(state.isDue(dismissed + 5 * day - 1))
        assertTrue(state.isDue(dismissed + 5 * day))
    }

    @Test
    fun `completed feedback is never requested again`() {
        val state = FeedbackPromptState(startedAt = start, completed = true)
        assertFalse(state.isDue(start + 5 * day))
        assertFalse(state.isDue(start + 365 * day))
    }

    @Test
    fun `unfinished external feedback still respects the five-day cooldown`() {
        val state = FeedbackPromptState(startedAt = start, lastPromptAt = start + 5 * day, pendingRating = 4)
        assertFalse(state.isDue(start + 6 * day))
        assertFalse(state.isDue(start + 10 * day - 1))
        assertTrue(state.isDue(start + 10 * day))
        assertFalse(state.copy(completed = true).isDue(start + 10 * day))
    }

    @Test
    fun `uninitialized state and clocks moving backwards do not prompt`() {
        assertFalse(FeedbackPromptState().isDue(start))
        assertFalse(FeedbackPromptState(startedAt = start).isDue(start - day))
        assertFalse(FeedbackPromptState(startedAt = start, lastPromptAt = start + 10 * day).isDue(start + 5 * day))
    }

    @Test
    fun `four and five stars route to Play and lower ratings route to email`() {
        (1..3).forEach { assertEquals(FeedbackDestination.EMAIL, feedbackDestination(it)) }
        (4..5).forEach { assertEquals(FeedbackDestination.PLAY_STORE, feedbackDestination(it)) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `unselected rating cannot launch a destination`() {
        feedbackDestination(0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `out of range rating cannot launch a destination`() {
        feedbackDestination(6)
    }
}
