package com.app.our.cskies.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.random.Random.Default.nextInt

object Converter {
    fun getImageUrl(iconCode:String):String{
     return Constants.IMG_URL + "${iconCode}@4x.png"
 };


   /* fun pickedDateFormatDate(dt:Date): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(dt)
    }*/
    /*fun pickedDateFormatTime(dt:Date): String {
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(dt)
    }*/
    fun generateRandomNumber():Int{
        return nextInt()
    }

    @RequiresApi(Build.VERSION_CODES.M)
   /* fun canelAlarm(context: Context, alert:String?, requestCode:Int)
    {
        var alarmMgr: AlarmManager? = null 
        lateinit var alarmIntent: PendingIntent
        alarmMgr = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context.applicationContext, AlarmReciver::class.java).putExtra(
            Constants.Alert,alert).let {
                intent -> PendingIntent.getBroadcast(context.applicationContext, requestCode, intent, PendingIntent.FLAG_IMMUTABLE )
            } alarmMgr?.cancel(alarmIntent)
    }*/
    fun isDaily(startTime: Long,endTime:Long):Boolean{
        return endTime-startTime >= 86400000
    }

}
