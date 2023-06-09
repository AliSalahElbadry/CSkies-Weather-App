package com.app.our.cskies.utils

import kotlin.math.roundToInt

object Converter {
    fun getImageUrl(iconCode:String):String{
     return Constants.IMG_URL + "${iconCode}.png"
 }
    fun getTemperature(temp:Int):Int{
       return when (Setting.temperature) {
           Setting.Temperature.F -> {
               temp*(9/5)+32
           }
           Setting.Temperature.K -> {
               temp+273
           }
           else -> {
               temp
           }
       }
    }
    fun getWindSpeed(w:Int):String{
       return if(Setting.wSpeed==Setting.WSpeed.MILE_HOUR)
        {
            "${(w/1609.344).roundToInt()} Mile/H"
        }else{
           "$w M/S"
        }
    }

}
