package com.app.our.cskies.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager

object UserStates {
    fun checkConnectionState(activity:Activity): Boolean {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        return nInfo != null && nInfo.isAvailable && nInfo.isConnected
    }
}