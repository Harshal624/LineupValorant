package com.harsh.lineupvalorant.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreVideosRepository {
    private val mAbilitiesCollection = Firebase.firestore.collection(ABILITIES)

    /* fun getAllVideos() = flow<List<Video>> {
         val snapshot = mAbilitiesCollection.get().await()
         val videos = snapshot.toObjects(Video::class.java)

         emit(videos)
     }.catch {
         //Do nothing for now
     }.flowOn(Dispatchers.IO)


     fun addVideo(video: Video) = flow<DocumentReference> {

         val videoRef = mAbilitiesCollection.add(video).await()
         emit(videoRef)

     }.catch {
         // If exception is thrown, emit failed state along with message.
     }.flowOn(Dispatchers.IO)*/

    fun getAllVideos(): List<Video> {
        val listOfVideos = ArrayList<Video>()
        mAbilitiesCollection.get().addOnSuccessListener {
            for (video in it) {
                listOfVideos.add(video.toObject(Video::class.java))
            }
            if (listOfVideos.isNotEmpty()) {
                return@addOnSuccessListener
            }
        }
        return emptyList()
    }

    companion object {
        val ABILITIES = "Abilities"
    }
}