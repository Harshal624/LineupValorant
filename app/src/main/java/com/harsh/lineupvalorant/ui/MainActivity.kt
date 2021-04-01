package com.harsh.lineupvalorant.ui

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.harsh.lineupvalorant.R
import com.harsh.lineupvalorant.data.sync.PeriodicSync
import com.harsh.lineupvalorant.utils.datastore.ShouldFetchDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/*
 *Upon very first launch of the app -> Perform hard sync i.e. retrieve all firestore data and put into local SQLite
 * If it's not a first launch + sqlite count != 0 -> Use workmanager to check newly added videos to firestore
 * Show notification e.g. 2 new videos found
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var shouldFetchDataStore: ShouldFetchDataStore

    private val viewModel: MainViewModel by viewModels()
    private var fetchingJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)

        setUpWorkManager();
    }

    private fun setUpWorkManager() {
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            PeriodicSync::class.java,
            1,
            TimeUnit.HOURS
        ).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "MYWORK",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

        val workmanager = WorkManager.getInstance(this).getWorkInfosForUniqueWork("MYWORK")
        val wm = workmanager.get()
        for (work in wm) {
            Timber.v("Work: ${work}")
        }
    }

    override fun onStart() {
        super.onStart()
        fetchingJob = lifecycleScope.launch {
            viewModel.fetchVideos()
        }
    }

    override fun onStop() {
        fetchingJob?.cancel()
        super.onStop()
    }
}