package com.app.our.cskies.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun formatDayTime(dt: Long): String {
        val date = Date(dt * 1000L)
        val formatter = SimpleDateFormat("hh:mm aa", Locale(Setting.getLang()))
        return formatter.format(date)
    }

    fun getDate(dt:Long):String {
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale(Setting.getLang()))
        return formatter.format(date)
    }
    fun getDateTimeAlert(dt:Long):String{
        val date = Date(dt)
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm aa")
        return sdf.format(date)
    }

    fun getDay(dt:Long):String{
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("EEEE", Locale(Setting.getLang()))
        return formatter.format(date)
    }
    fun getHour(dt:Long):String{
        val date= Date(dt * 1000L)
        val sdf = SimpleDateFormat("hh aa",Locale(Setting.getLang()))
        return sdf.format(date)
    }
}