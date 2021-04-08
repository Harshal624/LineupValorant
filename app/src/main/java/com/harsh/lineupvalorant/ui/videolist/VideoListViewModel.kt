package com.harsh.lineupvalorant.ui.videolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.harsh.lineupvalorant.api.DailyMotionApi
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.data.cache.VideoDao
import com.harsh.lineupvalorant.utils.datastore.CoreDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject


/*
 * If a video is fetched, set shouldFetch to false
 * Workmanager will set the shouldFetch to true after a period of time
 * Problems: 1.If the user is connected to wifi, fetch won't happen
 *           2.If shouldFetch is false + database is empty -> setShouldFetch to true
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoDao: VideoDao,
    private val dailyMotionApi: DailyMotionApi,
    private val coreDataStore: CoreDataStore
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val videosFlow = searchQuery.flatMapLatest {
        videoDao.getAllVideos(searchQuery = it)
    }

    val videoDetails: LiveData<List<Video>> = videosFlow.asLiveData()
}