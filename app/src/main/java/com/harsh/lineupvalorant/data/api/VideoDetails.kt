package com.harsh.lineupvalorant.data.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoDetails(
    @Expose
    @SerializedName("duration")
    val video_duration: Long,
    @Expose
    @SerializedName("thumbnail_small")
    val img_small: String,
    @Expose
    @SerializedName("thumbnail_medium")
    val img_medium: String,
    @Expose
    @SerializedName("thumbnail_large")
    val img_large: String
) {
}