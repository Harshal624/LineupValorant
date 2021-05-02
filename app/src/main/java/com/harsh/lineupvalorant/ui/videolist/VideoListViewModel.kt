package com.harsh.lineupvalorant.ui.videolist

import androidx.lifecycle.*
import com.harsh.lineupvalorant.data.cache.VideoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
    //Saved state handle here contains the saved state argumenets + safe args
    private val state: SavedStateHandle
) : ViewModel() {

    private val videoEventsChannel = Channel<VideoEvent>()
    val videoEvent = videoEventsChannel.receiveAsFlow()

    // val searchQuery = MutableStateFlow("")

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)

    val hideFavourites = MutableStateFlow(false)

    /*
     Here we can directly access the safe-args arguments via saved state handle
     The key should be same as the argument name
     We could use this variable to pre-fill filter dialog. Not being used for now
     */
    val videoType = state.get<String>("video_type")

    /*
    Cool this about saving the livedata in the saved state handle is we don't have to
    handle the set method
    Whenever we make changes to the searchquery it will automatically persist the current query in the
    save state
     */
    val searchQuery = state.getLiveData("searchQuery", "")


    /**
     * Returns a flow that switches to a new flow produced by transform function every time the original
     * flow emits a value. When the original flow emits a new value, the previous flow produced by
     * transform block is cancelled.
     */
    private val videosFlow = combine(
        //Converting the livedata to flow since livedata cannot be combined
        searchQuery.asFlow(),
        sortOrder,
        hideFavourites
    ) { query, sortOrder, hideFav ->
        Triple(query, sortOrder, hideFav)

    }.flatMapLatest {
        videoDao.getVideos(it.first, it.second, it.third)
    }

    val videoDetails = videosFlow.asLiveData()

    fun onFilterClick(videoType: String) {
        viewModelScope.launch {
            videoEventsChannel.send(VideoEvent.NavigateToFilterVideoScreen(videoType))
        }
    }
}

sealed class VideoEvent {
    data class NavigateToFilterVideoScreen(val videoType: String) : VideoEvent()
}

enum class SortOrder {
    BY_DATE, BY_VIEWS
}