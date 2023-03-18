package com.app.our.cskies.network.model

import com.google.gson.annotations.SerializedName

data class WeatherLocationData(

    @SerializedName("current") val current: Current,
    @SerializedName("daily")val daily: List<Daily>,
    @SerializedName("hourly")val hourly: List<Hourly>,
    @SerializedName("lat")val lat: Double,
    @SerializedName("lon")val lon: Double,
    @SerializedName("minutely") val minutely: List<Minutely>,
    @SerializedName("timezone")val timezone: String,
    @SerializedName("timezone_offset")val timezone_offset: Int
)