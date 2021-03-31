package com.harsh.lineupvalorant

import android.app.Application
import com.harsh.lineupvalorant.utils.CrashReportingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class LineupValorant : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        } else {
            Timber.plant(CrashReportingTree());
        }
    }
}