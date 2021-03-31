package com.harsh.lineupvalorant.ui.videolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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


/*
 * If a video is fetched, set shouldFetch to false
 * Workmanager will set the shouldFetch to true after a period of time
 * Problems: 1.If the user is connected to wifi, fetch won't happen
 *           2.If shouldFetch is false + database is empty -> setShouldFetch to true
 */
@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoDao: VideoDao,
    private val vimeoApi: VimeoApi,
    private val shouldFetchDataStore: ShouldFetchDataStore
) : ViewModel() {
    val videoDetails: LiveData<List<Video>> = videoDao.getAllVideos().asLiveData()

    init {
        viewModelScope.launch {
            val shouldFetch = shouldFetchDataStore.shouldFetch()
            Timber.v("shouldfetch value inside viewmodel initially: $shouldFetch")
            if (shouldFetch!!) {
                val collection = Firebase.firestore.collection("Abilities")
                val queryDoc = collection.get().await()
                val videos = queryDoc.toObjects(Video::class.java)
                Timber.v("is videos list empty? ${videos.isEmpty()}")

                for (video in videos) {
                    Timber.v(video.title)
                    val videoDetails = vimeoApi.getVideoDetails(video_id = video.video_url)
                    for (videoDetail in videoDetails) {
                        val singleVideo = video
                        singleVideo.img_small = videoDetail.img_small
                        singleVideo.img_medium = videoDetail.img_medium
                        singleVideo.img_large = videoDetail.img_large
                        singleVideo.video_duration = videoDetail.video_duration
                        Timber.v("Inserting single video: " + singleVideo.toString())
                        videoDao.insertSingleVideo(singleVideo)
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