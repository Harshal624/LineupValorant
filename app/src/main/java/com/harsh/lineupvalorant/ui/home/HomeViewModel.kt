package com.harsh.lineupvalorant.ui.home

import androidx.lifecycle.*
import com.harsh.lineupvalorant.data.api.VideoDetails
import com.harsh.lineupvalorant.data.api.VimeoApi
import com.harsh.lineupvalorant.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * We no long require to inject @Assisted
 * alpha 2.30+ has assisted included :D
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vimeoApi: VimeoApi,
    private val saveStateHandle: SavedStateHandle
) : ViewModel() {
    private val _videoLiveData = MutableLiveData<List<VideoDetails>>()
    val videoDetails: LiveData<List<VideoDetails>> = _videoLiveData


    init {
        viewModelScope.launch {
            val videoDetails = vimeoApi.getVideoDetails(Constants.TEST_VIDE_LINK)
            _videoLiveData.value = videoDetails
        }
    }
}