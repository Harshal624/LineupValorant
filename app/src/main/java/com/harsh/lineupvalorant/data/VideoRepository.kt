package com.harsh.lineupvalorant.data

import com.harsh.lineupvalorant.data.cache.VideoDao
import com.harsh.lineupvalorant.data.cache.VideoDatabase
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val videoDao: VideoDao,
    private val firestoreVideosRepository: FirestoreVideosRepository,
    private val videoDatabase: VideoDatabase
) {

    /* fun getVideos() = networkBoundResource(
         query = {
             videoDao.getAllVideos()
         },
         fetch = {
             firestoreVideosRepository.getAllVideos()
         },
         saveFetchResult = { videos ->
             videoDatabase.withTransaction {
                 videoDao.deleteAllVideos()
                 videoDao.insertVideos(videos)
             }
         }
     )*/

    fun getVideos() = firestoreVideosRepository.getAllVideos()
}