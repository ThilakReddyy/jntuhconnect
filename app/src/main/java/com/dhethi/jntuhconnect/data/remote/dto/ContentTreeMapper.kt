package com.dhethi.jntuhconnect.data.remote.dto

import com.dhethi.jntuhconnect.domain.model.ContentDoc
import com.dhethi.jntuhconnect.domain.model.ContentEntry
import com.dhethi.jntuhconnect.domain.model.ContentNode
import com.google.gson.JsonElement

/**
 * Rebuild the nested JSON tree returned by `/api/calendars` and `/api/syllabus` into a
 * typed [ContentNode].
 *
 * The two endpoints share a shape but end differently:
 *  - calendars end in `{ title: link }` objects (all-string values),
 *  - syllabus ends in `[ { title, link } ]` arrays.
 *
 * Both become [ContentNode.Documents] leaves; every other object is a
 * [ContentNode.Branch]. This mirrors the variable-depth walk the web frontend performs,
 * so a branch that collapses a level (e.g. syllabus rows with no regulation) still works.
 */
fun JsonElement.toContentNode(): ContentNode = when {
    isJsonArray -> ContentNode.Documents(asJsonArray.mapNotNull { it.toContentDocOrNull() })

    isJsonObject -> {
        val entries = asJsonObject.entrySet()
        if (entries.isNotEmpty() && entries.all { it.value.isJsonPrimitive }) {
            // { title: link } -> a leaf list of documents.
            ContentNode.Documents(entries.map { ContentDoc(it.key, it.value.asString) })
        } else {
            ContentNode.Branch(entries.map { ContentEntry(it.key, it.value.toContentNode()) })
        }
    }

    else -> ContentNode.Documents(emptyList())
}

private fun JsonElement.toContentDocOrNull(): ContentDoc? {
    if (!isJsonObject) return null
    val obj = getAsJsonObject()
    val title = obj.get("title")?.takeIf { it.isJsonPrimitive }?.asString ?: return null
    val link = obj.get("link")
        ?.takeIf { it.isJsonPrimitive }
        ?.asString
        ?.takeIf { it.isNotBlank() }
        ?: return null
    return ContentDoc(title, link)
}
