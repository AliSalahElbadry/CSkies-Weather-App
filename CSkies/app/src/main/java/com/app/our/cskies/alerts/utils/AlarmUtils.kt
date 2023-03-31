package com.app.our.cskies.alerts.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import com.app.our.cskies.alerts.alart_back_workers.AlertReceiver
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.utils.Constants
import com.app.our.cskies.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

object  AlarmUtils {

    fun setAlarm(context: Context, alert:Alert)
    {
        val alarmMgr:AlarmManager =context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent =  Intent(context, AlertReceiver::class.java).putExtra("alarm",alert)

        val alarmIntent =
            alert.getId()?.let {
                PendingIntent.getBroadcast(context,
                    it, intent, PendingIntent.FLAG_IMMUTABLE)
            }
        val fromDate = Calendar.getInstance()
        fromDate.timeInMillis=alert.fromDate.toLong()
        val toDate = Calendar.getInstance()
        toDate.timeInMillis=alert.toDate.toLong()
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,fromDate.timeInMillis,alarmIntent)
    }
    fun canelAlarm(context: Context, alert:Alert)
    {
        var alarmMgr: AlarmManager? = null
        alarmMgr = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent: PendingIntent = Intent(context.applicationContext, AlertReceiver::class.java).putExtra(
            "alarm",alert).let {
                intent -> PendingIntent.getBroadcast(context.applicationContext, alert.getId()!!, intent, PendingIntent.FLAG_IMMUTABLE )
        }
        try {
            alarmMgr.cancel(alarmIntent)
        }catch (e:java.lang.Exception){
            Log.e("","Alarm Not Found "+e.message)
        }
    }
}