package com.harsh.lineupvalorant

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.harsh.lineupvalorant.data.sync.PeriodicSync
import com.harsh.lineupvalorant.utils.CrashReportingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class LineupValorant : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        //Set up timber
        setUpTimberLogging()

        //Set up workmanager for video sync
        setUpWorkManager()

        //Set up notification channels
        createNotificationChannel()
    }

    private fun setUpWorkManager() {

        //Setting up constraints for workmanager
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //Sync videos every 1 hour which could be achieved by PeriodicWorkRequest
        //With a repeat interval of 1 hour
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            PeriodicSync::class.java,
            1,
            TimeUnit.HOURS
        ).setConstraints(constraint).build()

        //Enqueue the periodic work with a unique workname
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "VideoSyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    private fun setUpTimberLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        } else {
            Timber.plant(CrashReportingTree());
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val mChannelName = "video_channel"
            val mChannelDescription = "video_channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                PeriodicSync.VIDEO_NOTIFICATION_CHANNEL_ID,
                mChannelName,
                importance
            ).apply {
                description = mChannelDescription
                //Optional
                lightColor = Color.GREEN
                enableLights(true)
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}