package com.dhethi.jntuhconnect.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dhethi.jntuhconnect.presentation.theme.ThemeMode
import com.dhethi.jntuhconnect.domain.model.RecentDocument
import com.dhethi.jntuhconnect.domain.model.FeedbackPromptState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

/**
 * App-wide preferences and reminder state backed by DataStore. Injected as a
 * singleton via Hilt; the storage constructor also permits persistence tests.
 */
@Singleton
class AppPreferences internal constructor(
    private val dataStore: DataStore<Preferences>
) {
    @Inject
    constructor(@ApplicationContext context: Context) : this(context.dataStore)

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
        val NOTIFICATION_PERMISSION_REQUESTED =
            booleanPreferencesKey("notification_permission_requested")
        val RECENT_DOCUMENTS = stringPreferencesKey("recent_documents")
        val RESULT_NOTIFICATION_DEVICE_ID = stringPreferencesKey("result_notification_device_id")
        val RESULT_NOTIFICATION_ROLLS = stringSetPreferencesKey("result_notification_rolls")
        val FEEDBACK_STARTED_AT = longPreferencesKey("feedback_started_at")
        val FEEDBACK_LAST_PROMPT_AT = longPreferencesKey("feedback_last_prompt_at")
        val FEEDBACK_COMPLETED = booleanPreferencesKey("feedback_completed")
        val FEEDBACK_PENDING_RATING = intPreferencesKey("feedback_pending_rating")
    }

    private val gson = Gson()
    private val recentDocumentListType = object : TypeToken<List<RecentDocument>>() {}.type

    val themeMode: Flow<ThemeMode> = dataStore.data
        .map { ThemeMode.fromName(it[Keys.THEME_MODE]) }

    val notificationsEnabled: Flow<Boolean> = dataStore.data
        .map { it[Keys.NOTIFICATIONS] ?: false }

    val notificationPermissionRequested: Flow<Boolean> = dataStore.data
        .map { it[Keys.NOTIFICATION_PERMISSION_REQUESTED] ?: false }

    val feedbackPromptState: Flow<FeedbackPromptState> = dataStore.data
        .map(::decodeFeedbackPromptState)

    suspend fun initializeFeedbackPrompt(now: Long = System.currentTimeMillis()) {
        dataStore.edit {
            if (it[Keys.FEEDBACK_STARTED_AT] == null) it[Keys.FEEDBACK_STARTED_AT] = now
        }
    }

    // Atomically reserve the next prompt before showing UI, including across recreation.
    suspend fun claimFeedbackPrompt(now: Long = System.currentTimeMillis()): Boolean {
        var claimed = false
        dataStore.edit { preferences ->
            if (decodeFeedbackPromptState(preferences).isDue(now)) {
                preferences[Keys.FEEDBACK_LAST_PROMPT_AT] = now
                claimed = true
            }
        }
        return claimed
    }

    suspend fun setFeedbackRating(rating: Int) {
        require(rating in 1..5)
        dataStore.edit {
            if (it[Keys.FEEDBACK_COMPLETED] != true) it[Keys.FEEDBACK_PENDING_RATING] = rating
        }
    }

    suspend fun deferFeedbackPrompt(now: Long = System.currentTimeMillis()) {
        dataStore.edit {
            it[Keys.FEEDBACK_LAST_PROMPT_AT] = now
            it.remove(Keys.FEEDBACK_PENDING_RATING)
        }
    }

    // External email and Play Store activities cannot report submission to the app.
    // Only an explicit user confirmation marks feedback as completed.
    suspend fun completeFeedbackPrompt() {
        dataStore.edit {
            it[Keys.FEEDBACK_COMPLETED] = true
            it.remove(Keys.FEEDBACK_PENDING_RATING)
        }
    }

    private fun decodeFeedbackPromptState(preferences: Preferences) = FeedbackPromptState(
        startedAt = preferences[Keys.FEEDBACK_STARTED_AT] ?: 0,
        lastPromptAt = preferences[Keys.FEEDBACK_LAST_PROMPT_AT] ?: 0,
        completed = preferences[Keys.FEEDBACK_COMPLETED] ?: false,
        pendingRating = preferences[Keys.FEEDBACK_PENDING_RATING]?.takeIf { it in 1..5 }
    )

    val recentDocuments: Flow<List<RecentDocument>> = dataStore.data.map { preferences ->
        decodeRecentDocuments(preferences[Keys.RECENT_DOCUMENTS])
            .filter { it.isWithinLast24Hours() }
            .sortedByDescending(RecentDocument::openedAt)
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.NOTIFICATIONS] = enabled }
    }

    suspend fun markNotificationPermissionRequested() {
        dataStore.edit { it[Keys.NOTIFICATION_PERMISSION_REQUESTED] = true }
    }

    suspend fun resultNotificationDeviceId(): String {
        dataStore.data.first()[Keys.RESULT_NOTIFICATION_DEVICE_ID]?.let { return it }
        val deviceId = UUID.randomUUID().toString()
        dataStore.edit { preferences ->
            if (preferences[Keys.RESULT_NOTIFICATION_DEVICE_ID] == null) {
                preferences[Keys.RESULT_NOTIFICATION_DEVICE_ID] = deviceId
            }
        }
        return dataStore.data.first()[Keys.RESULT_NOTIFICATION_DEVICE_ID] ?: deviceId
    }

    suspend fun subscribedResultRolls(): Set<String> =
        dataStore.data.first()[Keys.RESULT_NOTIFICATION_ROLLS].orEmpty()

    suspend fun isResultRollSubscribed(rollNumber: String): Boolean {
        val preferences = dataStore.data.first()
        return rollNumber in preferences[Keys.RESULT_NOTIFICATION_ROLLS].orEmpty()
    }

    suspend fun markResultRollSubscribed(rollNumber: String) {
        dataStore.edit { preferences ->
            preferences[Keys.RESULT_NOTIFICATION_ROLLS] =
                preferences[Keys.RESULT_NOTIFICATION_ROLLS].orEmpty() + rollNumber
        }
    }

    suspend fun clearResultRollSubscriptions() {
        dataStore.edit { it.remove(Keys.RESULT_NOTIFICATION_ROLLS) }
    }

    suspend fun recordRecentDocument(document: RecentDocument) {
        dataStore.edit { preferences ->
            val now = System.currentTimeMillis()
            val current = decodeRecentDocuments(preferences[Keys.RECENT_DOCUMENTS])
                .filter { it.isWithinLast24Hours(now) }
                .filterNot { it.type == document.type }
            preferences[Keys.RECENT_DOCUMENTS] = gson.toJson(
                (listOf(document.copy(openedAt = now)) + current).take(2)
            )
        }
    }

    suspend fun clearRecentDocuments() {
        dataStore.edit { it.remove(Keys.RECENT_DOCUMENTS) }
    }

    suspend fun pruneExpiredRecentDocuments() {
        dataStore.edit { preferences ->
            val recent = decodeRecentDocuments(preferences[Keys.RECENT_DOCUMENTS])
                .filter { it.isWithinLast24Hours() }
            if (recent.isEmpty()) preferences.remove(Keys.RECENT_DOCUMENTS)
            else preferences[Keys.RECENT_DOCUMENTS] = gson.toJson(recent)
        }
    }

    private fun decodeRecentDocuments(json: String?): List<RecentDocument> =
        if (json.isNullOrBlank()) emptyList()
        else runCatching {
            gson.fromJson<List<RecentDocument>>(json, recentDocumentListType).orEmpty()
        }.getOrDefault(emptyList())
}
