package com.app.our.cskies.alerts.alart_back_workers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CloseHundleReciever:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
       val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        intent?.let {
            val id= it.getIntExtra("NOTIFICATION_ID",-1)
            if(id!=-1)
             notificationManager.cancel(id)
        }
    }
}