package com.harsh.lineupvalorant.data.api

import com.harsh.lineupvalorant.data.Video
import com.squareup.okhttp.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VimeoApi {

    @GET("api/v2/video/{video_id}.json")
    suspend fun getVideoDetails(@Path("video_id") video_id: String): retrofit2.Response<List<VideoDetails>>
}