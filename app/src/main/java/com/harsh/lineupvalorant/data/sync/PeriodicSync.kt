package com.harsh.lineupvalorant.data.sync

import android.app.PendingIntent
import android.content.Context
import android.net.ConnectivityManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harsh.lineupvalorant.R
import com.harsh.lineupvalorant.api.DailyMotionApi
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.data.cache.VideoDao
import com.harsh.lineupvalorant.utils.datastore.CoreDataStore
import com.harsh.lineupvalorant.utils.datastore.CoreDataStore.Companion.KEY_IS_FIRST_INSTALL
import com.harsh.lineupvalorant.utils.network.ConnectivityStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber


@HiltWorker
class PeriodicSync @AssistedInject constructor(

    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val videoDao: VideoDao,
    private val dailyMotionApi: DailyMotionApi,
    private val cm: ConnectivityManager

) :
    CoroutineWorker(appContext, workerParams) {

    //USE THIS SAME ID to update the notification from -> Loading to Completed
    private val mNotificationId = 100

    private val mCollectionName = "Abilities"

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            fetchVideos()
        }
        return Result.success()
    }


    private suspend fun fetchVideos() {
        if (ConnectivityStatus.getConnectionType(cm) != 0) {
            val dataStore = CoreDataStore(applicationContext)
            if (dataStore.isFirstAppInstall()) {
                //Is first install, show videos are loading notification
                Timber.v("Is first app install, showing videos are loading notification")
                showNotificationLoading()
            } else {
                Timber.v("Not a first app install, showing videos are syncing notification")
                dataStore.setBooleanDataInDataStore(
                    value = true,
                    dataStoreKey = KEY_IS_FIRST_INSTALL
                )
                //Show notification when video sync is started
                showNotificationSyncing()
            }


            //Retrieve videos from firestore
            val collection = Firebase.firestore.collection(mCollectionName)
            val queryDoc = collection.get().await()
            val videos = queryDoc.toObjects(Video::class.java)

            //Insert videos into local db
            videoDao.insertVideos(videos)

            //Retrieve video details e.g.video duration, thumbnail url's from dailymotion api
            for (video in videos) {
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

            //Show notification once video fetching and insertion is completed
            showNotificationCompleted()
        } else {
            //TODO If the app is first start and there's no internet connection, show notification -> No internet connection
            Timber.v("No internet connection")
        }
    }

    private fun showNotificationSyncing() {
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(mNotificationId, getNotificationSyncingBuilder().build())
        }
    }

    private fun showNotificationCompleted() {
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(mNotificationId, getNotificationCompletedBuilder().build())
        }
    }

    private fun showNotificationLoading() {
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(mNotificationId, getNotificationLoadingBuilder().build())
        }
    }


    //TODO Show notification for first time video load
    private fun getNotificationSyncingBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, VIDEO_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_loading_black)
            .setContentTitle("Sync video")
            .setContentText("Looking for new videos")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getNotificationClickPendingIntent())
    }

    private fun getNotificationLoadingBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, VIDEO_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_loading_black)
            .setContentTitle("Load video")
            .setContentText("Videos are loading")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getNotificationClickPendingIntent())
    }

    private fun getNotificationCompletedBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, VIDEO_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_check_black)
            .setContentTitle("Sync video")
            .setContentText("Videos synced successfully")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getNotificationClickPendingIntent())
    }

    private fun getNotificationClickPendingIntent(): PendingIntent {
        return NavDeepLinkBuilder(applicationContext).run {
            setGraph(R.navigation.nav_graph)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.videoListFragment)
                .createPendingIntent()
        }
    }

    companion object {
        const val VIDEO_NOTIFICATION_CHANNEL_ID = "video_fetch_notification_channel_id"
    }
}