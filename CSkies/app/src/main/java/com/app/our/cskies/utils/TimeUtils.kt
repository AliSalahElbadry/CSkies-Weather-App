package com.app.our.cskies.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun formatDayTime(dt: Long): String {
        val date = Date(dt * 1000L)
        val formatter = SimpleDateFormat("HH:mm", Locale("en"))
        return formatter.format(date)
    }
    fun formatTimeToArabic(dt: Long): String {
        val date = Date(dt * 1000L)
        val formatter = SimpleDateFormat("HH:mm", Locale("ar"))
        return formatter.format(date)
    }

    fun getDateArabic(dt:Long):String {
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale("ar"))
        return formatter.format(date)
    }
    fun getDateTime(dt:Long):String{
        return if(Setting.getLang()=="ar") {
            getDateArabic(dt)+" ${formatTimeToArabic(dt)}"

        }else{
            getDateFormat(dt)+" ${formatDayTime(dt)}"
        }
    }
    fun getDay(dt:Long):String{
        return if(Setting.getLang()=="ar"){
            getDayArabic(dt)
        }else{
            getDayFormat(dt)
        }
    }
    fun getDateFormat(dt:Long):String{
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale("en"))
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
    fun getDayFormat(dt:Long):String{
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("EEEE", Locale("en"))
        return formatter.format(date)
    }
    fun getDayArabic(dt:Long):String{
        val date= Date(dt * 1000L)
        val formatter = SimpleDateFormat("EEEE", Locale("ar"))
        return formatter.format(date)
    }
    fun getHour(dt:Long):String{
        return if(Setting.getLang()=="ar")
        {
            val sdf = SimpleDateFormat("HH aa", Locale("ar"))
             sdf.format(dt)
        }else{
            val sdf = SimpleDateFormat("HH aa",Locale("en"))
            sdf.format(dt)
        }
    }
    fun getCurrentDate(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(currentTime)
    }
    fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm",Locale(Setting.getLang()))
        return sdf.format(currentTime)
    }
    fun getDatePlus(num:Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, num)
        val tomorrow = calendar.time
        val sdf = SimpleDateFormat("dd-MM-yyyy",Locale(Setting.getLang()))
        return sdf.format(tomorrow)
    }
}