package com.app.our.cskies.Repository

import android.content.Context
import com.app.our.cskies.dp.LocalDataSource
import com.app.our.cskies.dp.LocalSourceImpl
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.ApiState
import com.app.our.cskies.network.RemoteSourceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow


class Repository private constructor(context: Context): RepositoryInterface {
    private val remoteSource:RemoteSourceImpl=RemoteSourceImpl.getInstance()!!
    private val localDataSource:LocalDataSource=LocalSourceImpl.getInstance(context)
    override suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        execlude: String,
        units: String,
        lang: String
    ): Flow<ApiState> {
       return try{
           remoteSource.getWeatherData(latitude,longitude, execlude, units, lang)
       }catch(e:Exception){
          flow { emit(ApiState.Failure(e.toString()))}
       }
    }

    override suspend fun insertLocation(location: LocationData) {
        localDataSource.insertLocation(location)
    }

    override suspend fun insertAlert(alert: Alert) {
      localDataSource.insertAlert(alert)
    }

    override suspend fun deleteLocation(location: Location) {
        localDataSource.deleteLocation(location)
    }

    override suspend fun deleteAlert(id: Int) {
        localDataSource.deleteAlert(id)
    }

    override  fun getListOfFavLocations(fav: Boolean): Flow<List<Location>> {
       return localDataSource.getListOfFavLocations(true)
    }

    override  fun selectDaysOfLocation(address: String): Flow<List<DayWeather>> {
       return localDataSource.selectDaysOfLocation(address)
    }

    override  fun selectHoursInLocation(address: String): Flow<List<HourWeather>> {
      return localDataSource.selectHoursInLocation(address)
    }

    override  fun getCurrentLocation(isCurrent: Boolean): Flow<Location> {
       return localDataSource.getCurrentLocation(true)
    }

    override  fun getListOfAlerts(): Flow<List<Alert>> {
      return localDataSource.getListOfAlerts()
    }

    override suspend fun getAlert(address: String, type: Int): Alert {
       return localDataSource.getAlert(address,type)
    }

   override suspend fun updateAlert(alert: Alert) {
        localDataSource.updateAlert(alert)
    }


    companion object{
        private var repository: Repository?=null
        fun getInstance(context: Context): Repository
        {
            if(repository ==null)
            {
                repository = Repository(context)
            }
            return repository!!
        }
    }
}