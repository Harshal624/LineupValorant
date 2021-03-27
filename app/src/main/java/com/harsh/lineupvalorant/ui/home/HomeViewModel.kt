package com.harsh.lineupvalorant.ui.home

import androidx.lifecycle.*
import com.harsh.lineupvalorant.data.api.VideoDetails
import com.harsh.lineupvalorant.data.api.VimeoRepository
import com.harsh.lineupvalorant.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * We no long require to inject @Assisted
 * alpha 2.30+ has assisted included :D
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vimeoRepository: VimeoRepository,
    private val saveStateHandle: SavedStateHandle
) : ViewModel() {

    private val _res = MutableLiveData<Resource<List<VideoDetails>>>()


    val res: LiveData<Resource<List<VideoDetails>>>
        get() = _res

    init {
        getVideoDetails("510284618")
    }
    private fun getVideoDetails(video_id: String) = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        vimeoRepository.getVideoDetails(video_id).let {
            if(it.isSuccessful){
                _res.postValue(Resource.success(it.body()))
            }
            else{
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }
}