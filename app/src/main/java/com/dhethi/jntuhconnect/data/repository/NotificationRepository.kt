package com.dhethi.jntuhconnect.data.repository

import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import com.dhethi.jntuhconnect.data.remote.JntuhConnectApi
import com.dhethi.jntuhconnect.data.remote.NotificationPreferencesRequest
import com.dhethi.jntuhconnect.data.remote.NotificationPreferencesResponse
import com.dhethi.jntuhconnect.data.remote.ResultSubscriptionRequest
import com.dhethi.jntuhconnect.telemetry.AppAnalytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val appPreferences: AppPreferences,
    private val api: JntuhConnectApi,
    private val analytics: FirebaseAnalytics
) {

    private suspend fun currentTopics(): Set<String> =
        appPreferences.subscribedNotificationTopics().ifEmpty { setOf(RESULT_NOTIFICATIONS_TOPIC) }

    private suspend fun applyTopics(from: Set<String>, to: Set<String>) {
        val messaging = FirebaseMessaging.getInstance()
        topicsToUnsubscribe(from, to).forEach { messaging.unsubscribeFromTopic(it).await() }
        topicsToSubscribe(from, to).forEach { messaging.subscribeToTopic(it).await() }
    }

    suspend fun setResultNotificationsEnabled(enabled: Boolean) {
        val topics = currentTopics()
        if (enabled) {
            applyTopics(from = emptySet(), to = topics)
        } else {
            applyTopics(from = topics, to = emptySet())
        }
        appPreferences.setNotificationsEnabled(enabled)
    }

    suspend fun restoreResultNotificationSubscription() {
        if (appPreferences.notificationsEnabled.first()) {
            applyTopics(from = emptySet(), to = currentTopics())
        }
    }

    suspend fun getNotificationTopicPreference(): NotificationPreferencesResponse {
        val response = api.getNotificationPreferences(
            deviceId = appPreferences.resultNotificationDeviceId()
        )
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()?.take(500)
            throw IOException(
                "Notification preference lookup failed with HTTP ${response.code()}: $errorBody"
            )
        }
        return response.body() ?: NotificationPreferencesResponse()
    }

    suspend fun saveNotificationTopicPreference(degrees: Set<String>, regulations: Set<String>) {
        val response = api.saveNotificationPreferences(
            NotificationPreferencesRequest(
                deviceId = appPreferences.resultNotificationDeviceId(),
                degrees = degrees.toList(),
                regulations = regulations.toList()
            )
        )
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()?.take(500)
            throw IOException(
                "Notification preference save failed with HTTP ${response.code()}: $errorBody"
            )
        }
        val topics = response.body()?.topics.orEmpty().toSet().ifEmpty { setOf(RESULT_NOTIFICATIONS_TOPIC) }
        applyTopics(from = currentTopics(), to = topics)
        appPreferences.setNotificationPreference(degrees, regulations, topics)
        if (!appPreferences.notificationsEnabled.first()) {
            appPreferences.setNotificationsEnabled(true)
        }
    }

    suspend fun clearNotificationTopicPreference() {
        val response = api.deleteNotificationPreferences(
            deviceId = appPreferences.resultNotificationDeviceId()
        )
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()?.take(500)
            throw IOException(
                "Notification preference reset failed with HTTP ${response.code()}: $errorBody"
            )
        }
        // Only touch Firebase if alerts are currently on; otherwise there's nothing
        // subscribed to unsubscribe from, and re-enabling later will apply the
        // now-cleared (global) preference via setResultNotificationsEnabled/restore.
        if (appPreferences.notificationsEnabled.first()) {
            applyTopics(from = currentTopics(), to = setOf(RESULT_NOTIFICATIONS_TOPIC))
        }
        appPreferences.clearNotificationPreference()
    }

    suspend fun subscribeToRoll(rollNumber: String) {
        Timber.tag(TAG).d("Requesting FCM token")
        val token = runCatching {
            FirebaseMessaging.getInstance().token.await()
        }.getOrElse { error ->
            Timber.tag(TAG).e(error, "FCM token request failed")
            AppAnalytics.logEvent(analytics, "push_subscription_failure")
            throw error
        }
        Timber.tag(TAG).d("FCM token acquired (${token.length} characters)")
        runCatching {
            saveRollSubscription(rollNumber, token)
        }.onFailure { error ->
            Timber.tag(TAG).e(error, "Result notification subscription save failed")
            AppAnalytics.logEvent(analytics, "push_subscription_failure")
            throw error
        }
        appPreferences.markResultRollSubscribed(rollNumber)
        Timber.tag(TAG).d("Result notification subscription saved")
        AppAnalytics.logEvent(analytics, "push_subscription_success")
    }

    suspend fun refreshRollSubscriptions(token: String) {
        appPreferences.subscribedResultRolls().forEach { rollNumber ->
            saveRollSubscription(rollNumber, token)
        }
    }

    suspend fun deleteAllRollSubscriptions() {
        // Clearing the app must not depend on network availability.
        appPreferences.clearResultRollSubscriptions()
        val response = api.deleteResultSubscriptions(
            deviceId = appPreferences.resultNotificationDeviceId()
        )
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()?.take(500)
            throw IOException(
                "Subscription deletion failed with HTTP ${response.code()}: $errorBody"
            )
        }
        Timber.tag(TAG).d("All result notification subscriptions deleted for this device")
    }

    private suspend fun saveRollSubscription(rollNumber: String, token: String) {
        val response = api.subscribeToResultUpdates(
            ResultSubscriptionRequest(
                deviceId = appPreferences.resultNotificationDeviceId(),
                deviceToken = token,
                rollNumber = rollNumber
            )
        )
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()?.take(500)
            throw IOException(
                "Subscription request failed with HTTP ${response.code()}: $errorBody"
            )
        }
    }

    companion object {
        private const val TAG = "ResultSubscription"
        const val RESULT_NOTIFICATIONS_TOPIC = "result-updates"
    }
}
