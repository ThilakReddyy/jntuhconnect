package com.dhethi.jntuhconnect.common

import com.google.gson.JsonParser
import retrofit2.HttpException

/**
 * Best-effort extraction of a human-readable message from a non-2xx response body.
 * The backend uses `{"message": ...}` for its JSONResponses and `{"detail": ...}` for
 * FastAPI validation errors; we try both.
 */
fun HttpException.serverMessage(): String? = try {
    val body = response()?.errorBody()?.string()
    if (body.isNullOrBlank()) null
    else {
        val obj = JsonParser.parseString(body).asJsonObject
        when {
            obj.has("message") && !obj.get("message").isJsonNull -> obj.get("message").asString
            obj.has("detail") && !obj.get("detail").isJsonNull -> obj.get("detail").asString
            else -> null
        }
    }
} catch (_: Exception) {
    null
}

/** Friendly message for common backend status codes. */
fun HttpException.friendlyMessage(): String {
    val server = serverMessage()
    if (!server.isNullOrBlank()) return server
    return when (code()) {
        423 -> "Server load is high right now. Please try again shortly."
        424 -> "JNTUH servers appear to be down. Please try again later."
        429 -> "Too many requests right now. Please try again in a moment."
        404 -> "We couldn't find that record."
        else -> "Something went wrong (${code()}). Please try again."
    }
}
