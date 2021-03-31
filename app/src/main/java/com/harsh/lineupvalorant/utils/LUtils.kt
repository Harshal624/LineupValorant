package com.harsh.lineupvalorant.utils

import java.time.Instant
import java.util.concurrent.TimeUnit

object LUtils {
    fun getCurrentTimeInEPOCH(): Long {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Instant.now().epochSecond
        } else {
            TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        }
    }

    fun getVideoId(url: String): String {
        //Function to get video id from a url
        return ""
    }
}