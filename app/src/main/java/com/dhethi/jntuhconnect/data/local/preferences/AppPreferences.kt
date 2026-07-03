package com.dhethi.jntuhconnect.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dhethi.jntuhconnect.presentation.theme.ThemeMode
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
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data
        .map { ThemeMode.fromName(it[Keys.THEME_MODE]) }

    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[Keys.NOTIFICATIONS] ?: true }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.NOTIFICATIONS] = enabled }
    }
}
