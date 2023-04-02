package com.app.our.cskies.Repository

import com.app.our.cskies.network.ApiState
import com.app.our.cskies.network.RemoteSource
import com.app.our.cskies.network.RemoteSourceImpl
import com.app.our.cskies.network.model.WeatherLocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteSource (private val location:WeatherLocationData):RemoteSource {



    override suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        execlude: String,
        units: String,
        lang: String
    ): Flow<ApiState> {
        return flow{
          emit(ApiState.Success(location))
        }

    }
}