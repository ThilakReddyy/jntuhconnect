package com.dhethi.jntuhconnect.data.remote.dto

import androidx.annotation.Keep
import com.dhethi.jntuhconnect.domain.model.ClassBacklogStudent
import com.dhethi.jntuhconnect.domain.model.ClassStudent
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

/**
 * The backend's class endpoint returns each student's `results` as an empty array `[]`
 * when the student has no synced marks, or as an object when they do. We type it as a
 * raw [JsonElement] and decode the object shape lazily in the mapper.
 */
private val classGson = Gson()

@Keep
data class ClassAcademicStudentDto(
    @SerializedName("details") val details: DetailsDto? = null,
    @SerializedName("results") val results: JsonElement? = null
)

@Keep
data class ClassBacklogStudentDto(
    @SerializedName("details") val details: DetailsDto? = null,
    @SerializedName("results") val results: JsonElement? = null
)

fun ClassAcademicStudentDto.toClassStudent(): ClassStudent? {
    val d = details ?: return null
    val academic = if (results != null && results.isJsonObject) {
        runCatching { classGson.fromJson(results, AcademicResultsDto::class.java).toResults() }.getOrNull()
    } else null
    return ClassStudent(details = d.toDetails(), result = academic)
}

fun ClassBacklogStudentDto.toClassBacklogStudent(): ClassBacklogStudent? {
    val d = details ?: return null
    val backlogs = if (results != null && results.isJsonObject) {
        runCatching { classGson.fromJson(results, BacklogResultDto::class.java).toBacklogResults() }.getOrNull()
    } else null
    return ClassBacklogStudent(details = d.toDetails(), backlogResult = backlogs)
}
