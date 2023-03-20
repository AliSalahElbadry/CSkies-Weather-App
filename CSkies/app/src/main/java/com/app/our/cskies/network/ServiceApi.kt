package com.app.our.cskies.network

import com.app.our.cskies.utils.Constants
import com.app.our.cskies.network.model.WeatherLocationData
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi{
    @GET("/data/2.5/onecall")
    suspend fun getWeatherInfo (@Query("lat") latitude:String,
                                @Query("lon") longitude:String,
                                @Query("exclude") execlude:String="weekly",
                                @Query("units") units:String="metric",
                                @Query("lang") lang:String="en",
                                @Query("appid") appid:String= Constants.apiKey): WeatherLocationData

}
