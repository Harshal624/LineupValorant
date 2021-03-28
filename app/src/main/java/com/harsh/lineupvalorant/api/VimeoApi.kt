package com.harsh.lineupvalorant.api

import retrofit2.http.GET
import retrofit2.http.Path

interface VimeoApi {

    @GET("api/v2/video/{video_id}.json")
    suspend fun getVideoDetails(@Path("video_id") video_id: String): List<VideoDetails>

    companion object {
        val VIMEO_BASE_URL = "https://vimeo.com/"
    }
}