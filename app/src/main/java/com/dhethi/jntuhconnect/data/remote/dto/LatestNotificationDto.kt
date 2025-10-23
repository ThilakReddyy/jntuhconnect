package com.dhethi.jntuhconnect.data.remote.dto


import com.dhethi.jntuhconnect.domain.model.LatestNotification
import com.google.gson.annotations.SerializedName

data class LatestNotificationDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("category")
    val category: String

)

fun LatestNotificationDto.toLatestNotification(): LatestNotification {
    return LatestNotification(
        date = date,
        link = link,
        releaseDate = releaseDate,
        title = title,
        category = category
    )
}