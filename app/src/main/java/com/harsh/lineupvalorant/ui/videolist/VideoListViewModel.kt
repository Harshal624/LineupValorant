package com.harsh.lineupvalorant.ui.videolist

import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harsh.lineupvalorant.data.FirestoreVideosRepository.Companion.ABILITIES
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.data.VideoRepository
import com.harsh.lineupvalorant.data.cache.VideoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val videoDao: VideoDao
) : ViewModel() {
    private val _videoLiveData = MutableLiveData<List<Video>>()
    val videoDetails: LiveData<List<Video>> = videoDao.getAllVideos().asLiveData()

    init {
        viewModelScope.launch {
            val collection = Firebase.firestore.collection(ABILITIES)
            val queryDoc = collection.get().await()
            val videos = queryDoc.toObjects(Video::class.java)
            videoDao.insertVideos(videos)
        }
    }
}