package com.harsh.lineupvalorant.data.sync

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class PeriodicSync(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    val firebaseDB = Firebase.firestore

    val TAG = "PeriodicSync"


    override fun doWork(): Result {
        var myString = "";
        val calendar = Calendar.getInstance()
        myString += "Day:${calendar.get(Calendar.DAY_OF_MONTH)}, Hour: ${calendar.get(Calendar.HOUR)}, Minute: ${calendar.get(Calendar.MINUTE)}\n"
        LineupSharedPref.init(applicationContext)
        myString += "\n\n${LineupSharedPref.setValue}"
        LineupSharedPref.setValue = myString
        return Result.success()
    }

}