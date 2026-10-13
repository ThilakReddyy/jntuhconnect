package com.dhethi.jntuhconnect.domain.model

private const val FIVE_DAYS_MILLIS = 5L * 24 * 60 * 60 * 1_000

enum class FeedbackDestination { PLAY_STORE, EMAIL }

fun feedbackDestination(rating: Int): FeedbackDestination {
    require(rating in 1..5)
    return if (rating >= 4) FeedbackDestination.PLAY_STORE else FeedbackDestination.EMAIL
}

data class FeedbackPromptState(
    val startedAt: Long = 0,
    val lastPromptAt: Long = 0,
    val completed: Boolean = false,
    val pendingRating: Int? = null
) {
    fun isDue(now: Long): Boolean = !completed && startedAt > 0 &&
        now - maxOf(startedAt, lastPromptAt) >= FIVE_DAYS_MILLIS
}
