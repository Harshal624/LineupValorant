package com.harsh.lineupvalorant.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoDetails(

    @Expose
    @SerializedName("id")
    val video_id: String,
    @Expose
    @SerializedName("duration")
    val video_duration: Long,
    @Expose
    @SerializedName("thumbnail_360_url")
    val img_small: String,
    @Expose
    @SerializedName("thumbnail_720_url")
    val img_medium: String,
    @Expose
    @SerializedName("thumbnail_1080_url")
    val img_large: String,
    @Expose
    @SerializedName("views_total")
    val total_views: Int

) {
}