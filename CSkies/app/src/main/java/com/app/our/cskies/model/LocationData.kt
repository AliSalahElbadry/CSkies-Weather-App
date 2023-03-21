package com.app.our.cskies.model

import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location

data class LocationData(val location:Location, var days:List<DayWeather>, var hours:List<HourWeather>)
