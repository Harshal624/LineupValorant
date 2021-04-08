package com.harsh.lineupvalorant.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.harsh.lineupvalorant.utils.datastore.CoreDataStore
import javax.inject.Inject


class PeriodicSync @Inject constructor(
    context: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val shouldFetchDataStore = CoreDataStore(applicationContext)
        if (shouldFetchDataStore.shouldFetch() == null) {
            shouldFetchDataStore.setShouldFetch(shouldFetch = true)
        }
        if (!shouldFetchDataStore.shouldFetch()!!) {
            shouldFetchDataStore.setShouldFetch(true)
        }
        return Result.success()
    }
}