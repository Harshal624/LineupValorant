package com.harsh.lineupvalorant.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.harsh.lineupvalorant.utils.datastore.ShouldFetchDataStore
import java.util.*
import javax.inject.Inject


class PeriodicSync @Inject constructor(
    context: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {
    val TAG = "PeriodicSync"
    override suspend fun doWork(): Result {
        val shouldFetchDataStore = ShouldFetchDataStore(applicationContext)
        if (shouldFetchDataStore.shouldFetch() == null) {
            shouldFetchDataStore.setShouldFetch(shouldFetch = true)
        }
        if (!shouldFetchDataStore.shouldFetch()!!) {
            shouldFetchDataStore.setShouldFetch(true)
        }
        var myString = "";
        val calendar = Calendar.getInstance()
        myString += "Day:${calendar.get(Calendar.DAY_OF_MONTH)}, Hour: ${calendar.get(Calendar.HOUR)}, Minute: ${
            calendar.get(
                Calendar.MINUTE
            )
        }\n"
        LineupSharedPref.init(applicationContext)
        myString += "\n\n${LineupSharedPref.setValue}"
        LineupSharedPref.setValue = myString
        return Result.success()
    }
}