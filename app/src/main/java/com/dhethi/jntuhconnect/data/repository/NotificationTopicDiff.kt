package com.dhethi.jntuhconnect.data.repository

/**
 * Pure topic-set diffing used when applying a new notification preference:
 * subscribe to what's newly wanted, unsubscribe from what's no longer wanted.
 */
fun topicsToSubscribe(current: Set<String>, desired: Set<String>): Set<String> = desired - current

fun topicsToUnsubscribe(current: Set<String>, desired: Set<String>): Set<String> = current - desired
