package com.app.our.cskies.shard_pref

import android.content.Context
import android.content.SharedPreferences
import com.app.our.cskies.model.Setting

class SharedPrefOps( context: Context) {

    private val sharedPreferences: SharedPreferences =context.getSharedPreferences("setting",Context.MODE_PRIVATE)
    fun insertInData()
    {
       val editor=sharedPreferences.edit()
        if(Setting.location!=null)
        {
            val res=if(Setting.location==Setting.Location.GPS) 0 else 1
            editor.putInt("location",res)
        }
        if(Setting.temperature!=null)
        {
            val res= when (Setting.temperature) {
                Setting.Temperature.C -> { 0 }
                Setting.Temperature.K -> {1}
                else -> {2}
            }
            editor.putInt("temperature",res)
        }
        if(Setting.lang!=null)
        {
            val res=if(Setting.lang==Setting.Lang.AR) 0 else 1
            editor.putInt("lang",res)
        }
        if(Setting.wSpeed!=null)
        {
            val res=if(Setting.wSpeed==Setting.WSpeed.M_S) 0 else 1
            editor.putInt("wSpeed",res)
        }
        if(Setting.notificationState!=null)
        {
            val res=if(Setting.notificationState==Setting.NotificationState.OFF) 0 else 1
            editor.putInt("notificationState",res)
        }
        editor.apply()
    }
    fun loadData(){
        Setting.location=
            if(sharedPreferences.getInt("location",-1)==0){
            Setting.Location.GPS
        }else{
            Setting.Location.MAP
        }
        Setting.lang=  if(sharedPreferences.getInt("lang",-1)==0){
            Setting.Lang.AR
        }else{
            Setting.Lang.EN
        }
        Setting.wSpeed=if(sharedPreferences.getInt("wSpeed",-1)==0){
            Setting.WSpeed.M_S
        }else{
            Setting.WSpeed.MILE_HOUR
        }
        Setting.temperature=when(sharedPreferences.getInt("temperature",-1))
        {
            0->Setting.Temperature.C
            1->Setting.Temperature.K
            else-> Setting.Temperature.F
        }
        Setting.notificationState=if(sharedPreferences.getInt("notificationState",-1)==0)
        {
            Setting.NotificationState.OFF
        }else{
            Setting.NotificationState.ON
        }
    }
}