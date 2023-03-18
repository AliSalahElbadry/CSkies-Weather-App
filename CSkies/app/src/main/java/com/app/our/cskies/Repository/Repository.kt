package com.app.our.cskies.Repository

import com.app.our.cskies.network.ApiState
import com.app.our.cskies.network.RemoteSourceImpl


class Repository private constructor(): RepositoryInterface {
    private val remoteSource:RemoteSourceImpl?=RemoteSourceImpl.getInstance()
    override suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        execlude: String,
        units: String,
        lang: String
    ): ApiState {
       return remoteSource?.getWeatherData(latitude,longitude, execlude, units, lang)!!
    }
    companion object{
        private var repository: Repository?=null
        fun getInstance(): Repository
        {
            if(repository ==null)
            {
                repository = Repository()
            }
            return repository!!
        }
    }
}