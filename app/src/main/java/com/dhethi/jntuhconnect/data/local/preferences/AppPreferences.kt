package com.dhethi.jntuhconnect.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dhethi.jntuhconnect.presentation.theme.ThemeMode
import com.dhethi.jntuhconnect.domain.model.RecentDocument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

/**
 * Lightweight app-wide preferences backed by DataStore: the chosen theme mode and a
 * local "notifications enabled" flag. Injected as a singleton via Hilt.
 */
@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
        val RECENT_DOCUMENTS = stringPreferencesKey("recent_documents")
    }

    private val gson = Gson()
    private val recentDocumentListType = object : TypeToken<List<RecentDocument>>() {}.type

    val themeMode: Flow<ThemeMode> = context.dataStore.data
        .map { ThemeMode.fromName(it[Keys.THEME_MODE]) }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[Keys.NOTIFICATIONS] ?: false }

    val recentDocuments: Flow<List<RecentDocument>> = context.dataStore.data.map { preferences ->
        decodeRecentDocuments(preferences[Keys.RECENT_DOCUMENTS])
            .filter { it.isWithinLast24Hours() }
            .sortedByDescending(RecentDocument::openedAt)
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.NOTIFICATIONS] = enabled }
    }

    suspend fun recordRecentDocument(document: RecentDocument) {
        context.dataStore.edit { preferences ->
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
        context.dataStore.edit { it.remove(Keys.RECENT_DOCUMENTS) }
    }

    suspend fun pruneExpiredRecentDocuments() {
        context.dataStore.edit { preferences ->
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
