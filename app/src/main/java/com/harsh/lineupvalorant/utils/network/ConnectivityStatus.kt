package com.harsh.lineupvalorant.utils.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.IntRange


/*
 * //TODO Replace with livedata so that we could observe the live changes of the internet connection
 * Could be cellular, wifi, vpn
 */
object ConnectivityStatus {

    @IntRange(from = 0, to = 3)
    fun getConnectionType(cm: ConnectivityManager): Int {
        var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = 2
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = 1
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        result = 3
                    }
                }
            }
        } else {
            cm.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = 2
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = 1
                    } else if (type == ConnectivityManager.TYPE_VPN) {
                        result = 3
                    }
                }
            }
        }
        return result
    }
}
