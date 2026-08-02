package com.dhethi.jntuhconnect.data.repository

import android.util.Log
import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import com.dhethi.jntuhconnect.data.remote.JntuhConnectApi
import com.dhethi.jntuhconnect.data.remote.ResultSubscriptionRequest
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val appPreferences: AppPreferences,
    private val api: JntuhConnectApi
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

    suspend fun subscribeToRoll(rollNumber: String) {
        Log.d(TAG, "Requesting FCM token for roll $rollNumber")
        val token = runCatching {
            FirebaseMessaging.getInstance().token.await()
        }.getOrElse { error ->
            Log.e(TAG, "FCM token request failed for roll $rollNumber", error)
            throw error
        }
        Log.d(TAG, "FCM token acquired for roll $rollNumber (${token.length} characters)")
        saveRollSubscription(rollNumber, token)
        appPreferences.markResultRollSubscribed(rollNumber)
        Log.d(TAG, "Result notification subscription saved for roll $rollNumber")
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
        Log.d(TAG, "All result notification subscriptions deleted for this device")
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
