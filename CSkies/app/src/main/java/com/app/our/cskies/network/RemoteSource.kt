package com.app.our.cskies.network

import kotlinx.coroutines.flow.Flow

interface RemoteSource {

   suspend fun getWeatherData(latitude:String,
                       longitude:String,
                       execlude:String="weekly",
                       units:String="metric",
                       lang:String="en",):Flow<ApiState>
}