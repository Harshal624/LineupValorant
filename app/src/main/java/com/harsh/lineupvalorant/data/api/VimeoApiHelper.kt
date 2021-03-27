package com.harsh.lineupvalorant.data.api

import com.harsh.lineupvalorant.data.Video
import retrofit2.Response

interface VimeoApiHelper {
    suspend fun getVideoDetails(video_id: String): Response<List<VideoDetails>>
}