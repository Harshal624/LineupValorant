package com.harsh.lineupvalorant.data.api

import javax.inject.Inject

class VimeoRepository @Inject constructor(
    private val apiHelper: VimeoApiHelper
) {
    suspend fun getVideoDetails(video_id: String) = apiHelper.getVideoDetails(video_id)
}