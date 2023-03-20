package com.app.our.cskies.Repository

import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.network.model.WeatherLocationData

class FactoryTransformer {

    fun mapWeatherNetworkToWeatherApp(weatherLocationData: WeatherLocationData):Triple<Location,DayWeather,HourWeather>
    {
        TODO()
    }

}