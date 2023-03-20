package com.app.our.cskies.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteSourceImpl private constructor():RemoteSource {
    override suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        execlude: String,
        units: String,
        lang: String
    ): Flow<ApiState> {
        return try{
              flow{emit(ApiState.Success(
                  Retrofit.myService.getWeatherInfo(
                      latitude,
                      longitude,
                      execlude,
                      units,
                      lang
                  )
              ))}
        }catch (e:Exception) {
          flow{emit(  ApiState.Failure(e.message?:"Fail"))}
        }
    }
    companion object{
        private var remoteSource:RemoteSourceImpl?=null
        fun getInstance():RemoteSourceImpl?
        {
            if(remoteSource==null)
            {
                remoteSource=RemoteSourceImpl()
            }
            return remoteSource
        }
    }
}