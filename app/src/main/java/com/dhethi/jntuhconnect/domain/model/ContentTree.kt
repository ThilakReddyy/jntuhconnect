package com.dhethi.jntuhconnect.domain.model

/**
 * A generic, variable-depth content tree used by the academic calendars and syllabus
 * screens. The backend returns nested JSON (academic year → degree → year → calendar,
 * or degree → regulation → category → documents) whose depth varies per branch, so the
 * screens walk it dynamically the same way the web frontend does.
 */
sealed interface ContentNode {

    /** A folder level: ordered, named children that drill down further. */
    data class Branch(val children: List<ContentEntry>) : ContentNode

    /** A leaf: a list of openable documents (PDF links). */
    data class Documents(val docs: List<ContentDoc>) : ContentNode
}

/** A named child within a [ContentNode.Branch]. */
data class ContentEntry(val label: String, val node: ContentNode)

/** A single openable document. */
data class ContentDoc(val title: String, val link: String)
