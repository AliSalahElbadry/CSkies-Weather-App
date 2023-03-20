package com.app.our.cskies.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun formatDayTime(dt: Long): String {
        val date = Date(dt * 1000L)
        val formatter = SimpleDateFormat("HH:mm")
        return formatter.format(date)
    }
    fun formatTimeToArabic(dt: Long): String {
        val date = Date(dt * 1000L)
        val formatter = SimpleDateFormat("HH:mm", Locale("ar"))
        return formatter.format(date)
    }
    fun getDateFormat(dt:Long):String{
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        return formatter.format(date)
    }
    fun formatDateAlert(dt:Long):String{
        val date= Date(dt )
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(date)
    }
    fun formatTimeAlert(dt:Long):String{
        val date = Date(dt)
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(date)
    }
    fun getDateArabic(dt:Long):String {
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale("ar"))
        return formatter.format(date)
    }
    fun getDayFormat(dt:Long):String{
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("EEEE")
        return formatter.format(date)
    }
    fun getDayArabic(dt:Long):String{
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("EEEE", Locale("ar"))
        return formatter.format(date)
    }
    fun getCurrentDate(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(currentTime)
    }
    fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(currentTime)
    }
    fun getCurrentDatePlus(num:Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, num)
        val tomorrow = calendar.time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(tomorrow)
    }
}