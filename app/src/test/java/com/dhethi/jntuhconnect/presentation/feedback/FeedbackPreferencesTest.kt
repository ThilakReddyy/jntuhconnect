package com.dhethi.jntuhconnect.presentation.feedback

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class FeedbackPreferencesTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()
    private val storeFile by lazy { temporaryFolder.newFile("feedback.preferences_pb") }
    private var storeJob = SupervisorJob()
    private val day = 24L * 60 * 60 * 1_000
    private val start = 1_700_000_000_000L

    private fun preferences() = AppPreferences(
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(storeJob + Dispatchers.IO),
            produceFile = { storeFile }
        )
    )

    @After
    fun closeStore() = runBlocking { storeJob.cancelAndJoin() }

    private suspend fun reopen(): AppPreferences {
        storeJob.cancelAndJoin()
        storeJob = SupervisorJob()
        return preferences()
    }

    @Test
    fun `launching again does not reset the first-use timestamp`() = runBlocking {
        val prefs = preferences()
        prefs.initializeFeedbackPrompt(start)
        prefs.initializeFeedbackPrompt(start + 4 * day)
        assertEquals(start, prefs.feedbackPromptState.first().startedAt)
        assertFalse(prefs.claimFeedbackPrompt(start + 5 * day - 1))
        assertTrue(prefs.claimFeedbackPrompt(start + 5 * day))
    }

    @Test
    fun `concurrent claims reserve only one reminder`() = runBlocking {
        val prefs = preferences()
        prefs.initializeFeedbackPrompt(start)
        val claims = (1..10).map {
            async(Dispatchers.IO) { prefs.claimFeedbackPrompt(start + 5 * day) }
        }.awaitAll()
        assertEquals(1, claims.count { it })
        assertEquals(start + 5 * day, prefs.feedbackPromptState.first().lastPromptAt)
    }

    @Test
    fun `unfinished email handoff survives restart and is not marked complete`() = runBlocking {
        val prefs = preferences()
        prefs.initializeFeedbackPrompt(start)
        assertTrue(prefs.claimFeedbackPrompt(start + 5 * day))
        prefs.setFeedbackRating(2)

        val restored = reopen()
        val state = restored.feedbackPromptState.first()
        assertEquals(2, state.pendingRating)
        assertFalse(state.completed)
        assertFalse(restored.claimFeedbackPrompt(start + 6 * day))
        assertTrue(restored.claimFeedbackPrompt(start + 10 * day))
        assertEquals(2, restored.feedbackPromptState.first().pendingRating)
    }

    @Test
    fun `not now clears handoff and persists a new five-day wait`() = runBlocking {
        val prefs = preferences()
        prefs.initializeFeedbackPrompt(start)
        assertTrue(prefs.claimFeedbackPrompt(start + 5 * day))
        prefs.setFeedbackRating(5)
        val deferredAt = start + 6 * day
        prefs.deferFeedbackPrompt(deferredAt)

        val restored = reopen()
        val state = restored.feedbackPromptState.first()
        assertEquals(null, state.pendingRating)
        assertFalse(state.completed)
        assertFalse(restored.claimFeedbackPrompt(deferredAt + 5 * day - 1))
        assertTrue(restored.claimFeedbackPrompt(deferredAt + 5 * day))
    }

    @Test
    fun `confirmed feedback stays complete across restart and future rating writes`() = runBlocking {
        val prefs = preferences()
        prefs.initializeFeedbackPrompt(start)
        prefs.setFeedbackRating(4)
        prefs.completeFeedbackPrompt()

        val restored = reopen()
        restored.setFeedbackRating(5)
        val state = restored.feedbackPromptState.first()
        assertTrue(state.completed)
        assertEquals(null, state.pendingRating)
        assertFalse(restored.claimFeedbackPrompt(start + 365 * day))
    }
}
