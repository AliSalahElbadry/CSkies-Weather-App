package com.app.our.cskies.network

class RemoteSourceImpl private constructor():RemoteSource {
    override suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        execlude: String,
        units: String,
        lang: String
    ): ApiState {
        return try{
              ApiState.Success(
                  Retrofit.myService.getWeatherInfo(
                      latitude,
                      longitude,
                      execlude,
                      units,
                      lang
                  )
              )
        }catch (e:Exception) {
            ApiState.Failure(e.message?:"Fail")
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