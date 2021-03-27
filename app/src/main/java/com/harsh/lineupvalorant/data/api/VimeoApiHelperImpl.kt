package com.harsh.lineupvalorant.data.api

import retrofit2.Response
import javax.inject.Inject

class VimeoApiHelperImpl @Inject constructor(
    private val apiService: VimeoApi,
): VimeoApiHelper {
    override suspend fun getVideoDetails(video_id: String): Response<List<VideoDetails>> = apiService.getVideoDetails(video_id)
}