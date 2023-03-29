package com.app.our.cskies.network

import android.system.ErrnoException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.ConnectException

class RemoteSourceImpl private constructor():RemoteSource {
    override suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        execlude: String,
        units: String,
        lang: String
    ): Flow<ApiState> {
        return try{
              flow{
                  emit(ApiState.Success(
                  Retrofit.myService.getWeatherInfo(
                      latitude,
                      longitude,
                      execlude,
                      units,
                      lang
                  )
              ))}
        }catch (e:IOException) {
          flow{emit(  ApiState.Failure(e.message?:"Fail"))}
        }
        catch (er: ErrnoException){
            flow{emit(  ApiState.Failure(er.message?:"Fail"))}
        }
        catch (c: ConnectException)
        {
            flow{emit(  ApiState.Failure(c.message?:"Fail"))}
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