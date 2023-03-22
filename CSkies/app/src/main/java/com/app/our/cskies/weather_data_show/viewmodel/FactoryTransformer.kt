package com.app.our.cskies.weather_data_show.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.model.WeatherLocationData
import com.app.our.cskies.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import kotlinx.coroutines.flow.MutableStateFlow
import java.sql.Time

class FactoryTransformer(val context: Context) {

    var data:LocationData?=null
    var stateFlow= MutableStateFlow<Int>(0)
    var numOfImages=0
    fun mapWeatherNetworkToWeatherApp(weatherLocationData: WeatherLocationData,isCurrent:Boolean,isFavorite:Boolean)
    {
        var location=Location(
            Setting.getLang(),
            TimeUtils.getDate(weatherLocationData.current.dt.toLong())+" "+TimeUtils.formatDayTime(weatherLocationData.current.dt.toLong()),

            UserCurrentLocation.getAddress(context,isFavorite),
            weatherLocationData.current.weather[0].description,
            isFavorite,
            weatherLocationData.current.temp.toInt(),
            weatherLocationData.current.pressure,
            weatherLocationData.current.humidity,
            weatherLocationData.current.clouds,
            Converter.getImageUrl(weatherLocationData.current.weather[0].icon),
            isCurrent,
            weatherLocationData.current.wind_speed.toInt()
        )

        var daily= mutableListOf<DayWeather>()
        var hourly= mutableListOf<HourWeather>()
        weatherLocationData.daily.forEach {
            val day=DayWeather(TimeUtils.getDay(it.dt.toLong()),
                UserCurrentLocation.getAddress(context,isFavorite),
                it.temp.max.toInt(),
                it.temp.min.toInt(),
                it.weather[0].description,
                Converter.getImageUrl(it.weather[0].icon))
            daily.add(day)
        }
       for(i in 0..23)
       {
           val hour=HourWeather(UserCurrentLocation.getAddress(context,isFavorite),
               TimeUtils.getHour(weatherLocationData.hourly[i].dt.toLong()+i),
               weatherLocationData.hourly[i].temp.toInt(),
               Converter.getImageUrl(weatherLocationData.hourly[i].weather[0].icon))
           hourly.add(hour)
       }
        data=LocationData(location,daily,hourly)
        dlownloadImages()
    }
    private fun dlownloadImages() {

        Glide.with(context)
            .asBitmap()
            .load(data!!.location.icon).into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                    data!!.location.setImageBitmap(resource)
                    numOfImages++
                    stateFlow.value=numOfImages
                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
        data!!.days.forEach{
            Glide.with(context)
                .asBitmap()
                .load(it.icon).into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                        it.setImageBitmap(resource)
                        numOfImages++
                        stateFlow.value=numOfImages
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
        data!!.hours.forEach{
            Glide.with(context)
                .asBitmap()
                .load(it.icon).into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                        it.setImageBitmap(resource)
                        numOfImages++
                        stateFlow.value=numOfImages
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
    }
    fun getLocationData():LocationData{
        return data!!
    }
}