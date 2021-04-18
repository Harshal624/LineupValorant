package com.harsh.lineupvalorant.api

import retrofit2.http.GET
import retrofit2.http.Path

interface DailyMotionApi {

    @GET("video/{video_id}?fields=$fieldsToBeUsed")
    suspend fun getVideoDetails(
        @Path("video_id") video_id: String,
    ): VideoDetails

    companion object {

        const val DAILYMOTION_BASE_URL = "https://api.dailymotion.com/"
        const val fieldsToBeUsed =
            "id,duration,views_total,thumbnail_1080_url,thumbnail_720_url,thumbnail_360_url"

    }
}