package com.harsh.lineupvalorant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harsh.lineupvalorant.api.VimeoApi
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.data.cache.VideoDao
import com.harsh.lineupvalorant.utils.datastore.ShouldFetchDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoDao: VideoDao,
    private val vimeoApi: VimeoApi,
    private val shouldFetchDataStore: ShouldFetchDataStore
) : ViewModel() {
    init {

    }

    fun fetchVideos() {
        viewModelScope.launch {
            if (shouldFetchDataStore.shouldFetch() == null) {
                Timber.v("shouldfetch Value is null...setting to true")
                shouldFetchDataStore.setShouldFetch(true)
            }
            val shouldFetch = shouldFetchDataStore.shouldFetch()
            Timber.v("shouldfetch value inside viewmodel initially: $shouldFetch")
            if (shouldFetch!!) {
                val collection = Firebase.firestore.collection("Abilities")
                val queryDoc = collection.get().await()
                val videos = queryDoc.toObjects(Video::class.java)
                Timber.v("is videos list empty? ${videos.isEmpty()}")
                videoDao.insertVideos(videos)
                for (video in videos) {
                    Timber.v(video.title)
                    val videoDetails = vimeoApi.getVideoDetails(video_id = video.video_url)
                    for (videoDetail in videoDetails) {
                        videoDao.updateVideoDetails(
                            video_url = videoDetail.video_url,
                            videoDuration = videoDetail.video_duration,
                            imgSmall = videoDetail.img_small,
                            imgMedium = videoDetail.img_medium,
                            imgLarge = videoDetail.img_large
                        )
                    }
                }
                /*
                Once the video fetching is completed, set should fetch to false
                 */
                shouldFetchDataStore.setShouldFetch(false)
            }
        }
    }
}