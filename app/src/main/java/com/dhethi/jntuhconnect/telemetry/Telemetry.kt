package com.dhethi.jntuhconnect.telemetry

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * Roll numbers are 10 alphanumeric characters. Over-redacting an unrelated
 * 10-char token is an acceptable cost; missing a real roll number in a log
 * message that reaches Crashlytics is not (see CLAUDE.md/SECURITY.md: avoid
 * logging FCM tokens, academic payloads, signing values, or service
 * credentials).
 */
private val ROLL_NUMBER_REGEX = Regex("\\b[0-9A-Za-z]{10}\\b")

private val SENSITIVE_KEYS = setOf(
    "fcm_token",
    "hall_ticket",
    "hallticket",
    "roll_number",
    "rollnumber",
    "marks",
    "sgpa",
    "cgpa",
    "subjects",
    "student_name",
    "name",
)

fun scrubText(text: String): String = ROLL_NUMBER_REGEX.replace(text, "[REDACTED]")

fun scrubPII(params: Map<String, Any>): Map<String, Any> =
    params.mapValues { (key, value) ->
        if (key.lowercase() in SENSITIVE_KEYS) "[REDACTED]" else value
    }

/**
 * Only WARN/ERROR are forwarded to Crashlytics — DEBUG/INFO/VERBOSE stay
 * local to logcat (via the DebugTree in debug builds; dropped entirely in
 * release, matching Crashlytics' own default uncaught-exception behavior).
 */
class CrashlyticsTree : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean =
        priority >= Log.WARN

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log("${tag ?: "App"}: ${scrubText(message)}")
        if (t != null) {
            crashlytics.recordException(t)
        }
    }
}

object AppAnalytics {
    private val ALLOWED_EVENTS = setOf(
        "result_lookup_success",
        "result_lookup_failure",
        "push_subscription_success",
        "push_subscription_failure",
        "notification_opened",
        "tool_used",
    )

    fun logEvent(analytics: FirebaseAnalytics, name: String, params: Map<String, Any> = emptyMap()) {
        require(name in ALLOWED_EVENTS) { "Unlisted analytics event: $name" }
        val bundle = Bundle()
        scrubPII(params).forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                else -> bundle.putString(key, value.toString())
            }
        }
        analytics.logEvent(name, bundle)
    }
}
