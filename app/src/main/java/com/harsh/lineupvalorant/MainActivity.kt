package com.harsh.lineupvalorant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.harsh.lineupvalorant.data.sync.PeriodicSync
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

/*
 *Upon very first launch of the app -> Perform hard sync i.e. retrieve all firestore data and put into local SQLite
 * If it's not a first launch + sqlite count != 0 -> Use workmanager to check newly added videos to firestore
 * Show notification e.g. 2 new videos found
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            PeriodicSync::class.java,
            1,
            TimeUnit.HOURS
        ).setConstraints(constraints).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("MYWORK",ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest)

        val workmanager = WorkManager.getInstance(this).getWorkInfosForUniqueWork("MYWORK")
        val wm = workmanager.get()
        for(work in wm){
            Timber.v("Work: ${work}")
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                //HomeFragment selected
            } else {
                //FavouritesFragment selected
            }
        }
    }

}