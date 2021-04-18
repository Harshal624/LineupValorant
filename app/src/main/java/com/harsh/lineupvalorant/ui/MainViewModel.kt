package com.harsh.lineupvalorant.ui

import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harsh.lineupvalorant.api.DailyMotionApi
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.data.cache.VideoDao
import com.harsh.lineupvalorant.utils.datastore.CoreDataStore
import com.harsh.lineupvalorant.utils.network.ConnectivityStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoDao: VideoDao,
    private val dailyMotionApi: DailyMotionApi,
    private val coreDataStore: CoreDataStore,
    private val cm: ConnectivityManager,
    //  private val fieldListForDailyMotion: List<String>
) : ViewModel() {

    fun fetchVideos() {
        if (ConnectivityStatus.getConnectionType(cm) != 0) {
            viewModelScope.launch {
                if (coreDataStore.shouldFetch() == null) {
                    Timber.v("shouldfetch Value is null...setting to true")
                    coreDataStore.setShouldFetch(true)
                }
                val shouldFetch = coreDataStore.shouldFetch()
                Timber.v("shouldfetch value inside viewmodel initially: $shouldFetch")
                if (shouldFetch!!) {
                    val collection = Firebase.firestore.collection("Abilities")
                    val queryDoc = collection.get().await()
                    val videos = queryDoc.toObjects(Video::class.java)
                    Timber.v("is videos list empty? ${videos.isEmpty()}")
                    videoDao.insertVideos(videos)
                    for (video in videos) {
                        Timber.v(video.title)
                        val videoDetails =
                            dailyMotionApi.getVideoDetails(video_id = video.video_url)
                            videoDao.updateVideoDetails(
                                video_url = videoDetails.video_id,
                                videoDuration = videoDetails.video_duration,
                                imgSmall = videoDetails.img_small,
                                imgMedium = videoDetails.img_medium,
                                imgLarge = videoDetails.img_large,
                                totalViews = videoDetails.total_views
                            )
                    }
                    /*
                    Once the video fetching is completed, set should fetch to false
                    Also check if the database is empty(temp solution) //TODO think about this
                    For some reason, on the very first start of the app, the videos doesn't get fetched from
                    firestore
                     */
                    val video_count = videoDao.getVideoCount()
                    Timber.v("Video count: $video_count")
                    if (video_count != 0) {
                        coreDataStore.setShouldFetch(false)
                    }
                }
            }
        } else {
            Timber.v("No internet connection")
        }

    }
}