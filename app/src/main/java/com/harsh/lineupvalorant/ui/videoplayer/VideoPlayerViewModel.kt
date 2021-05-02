package com.harsh.lineupvalorant.ui.videoplayer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.harsh.lineupvalorant.data.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel() {

    /*
     Here we can directly access the safe-args arguments via saved state handle
     The key should be same as the argument name
     */
    val video = state.get<Video>("video")

    var videoUrl = state.get<String>("videoUrl") ?: video?.video_url ?: ""
        //Storing the videoUrl in savedinstancestate and when our app gets recreated(process death), we try to read that url from
        //the saved state handle
        set(value) {
            field = value
            state.set("videoUrl", videoUrl)
        }
}