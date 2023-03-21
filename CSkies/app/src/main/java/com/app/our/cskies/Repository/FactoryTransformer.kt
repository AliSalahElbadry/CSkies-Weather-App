package com.app.our.cskies.Repository

import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.model.WeatherLocationData
import com.app.our.cskies.utils.Converter
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.TimeUtils

class FactoryTransformer() {

    fun mapWeatherNetworkToWeatherApp(weatherLocationData: WeatherLocationData):LocationData
    {
        var location=Location(
            Setting.getLang(),
            TimeUtils.getDateTime(weatherLocationData.current.dt.toLong()),"",
            weatherLocationData.current.weather[0].description,false,weatherLocationData.current.temp.toInt(),
            weatherLocationData.current.pressure,weatherLocationData.current.humidity,weatherLocationData.current.clouds,
            Converter.getImageUrl(weatherLocationData.current.weather[0].icon),false,weatherLocationData.current.wind_speed.toInt()
        )

        var daily= mutableListOf<DayWeather>()
        var hourly= mutableListOf<HourWeather>()
        weatherLocationData.daily.forEach {
            val day=DayWeather(TimeUtils.getDay(it.dt.toLong()),"",it.temp.max.toInt(),it.temp.min.toInt(),it.weather[0].description,
                Converter.getImageUrl(it.weather[0].icon))
            daily.add(day)
        }
       for(i in 0..23)
       {
           val hour=HourWeather("",TimeUtils.getHour(weatherLocationData.hourly[i].dt.toLong()),
           weatherLocationData.hourly[i].temp.toInt(),Converter.getImageUrl(weatherLocationData.hourly[i].weather[0].icon))
           hourly.add(hour)
       }
        return  LocationData(location,daily,hourly)
    }
}