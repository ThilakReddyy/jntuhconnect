package com.dhethi.jntuhconnect.data.repository

import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val appPreferences: AppPreferences
) {

    suspend fun setResultNotificationsEnabled(enabled: Boolean) {
        val messaging = FirebaseMessaging.getInstance()
        if (enabled) {
            messaging.subscribeToTopic(RESULT_NOTIFICATIONS_TOPIC).await()
        } else {
            messaging.unsubscribeFromTopic(RESULT_NOTIFICATIONS_TOPIC).await()
        }
        appPreferences.setNotificationsEnabled(enabled)
    }

    suspend fun restoreResultNotificationSubscription() {
        if (appPreferences.notificationsEnabled.first()) {
            FirebaseMessaging.getInstance()
                .subscribeToTopic(RESULT_NOTIFICATIONS_TOPIC)
                .await()
        }
    }

    companion object {
        const val RESULT_NOTIFICATIONS_TOPIC = "result-updates"
    }
}
