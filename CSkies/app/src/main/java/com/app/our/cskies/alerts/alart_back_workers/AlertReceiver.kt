package com.app.our.cskies.alerts.alart_back_workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.app.our.cskies.shard_pref.SharedPrefOps

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        context.startService(Intent(context,MyService::class.java).putExtra("alarm",intent.getSerializableExtra("alarm")))
    }

}