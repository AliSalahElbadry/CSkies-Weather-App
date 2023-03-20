package com.app.our.cskies.dp

import android.content.Context
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalSourceImpl private constructor(context: Context) :LocalDataSource {
    private val locationOps=AppDataBase.getInstance(context).getLocationDao()

    companion object{
        private  var localDataSource: LocalSourceImpl?=null
        fun getInstance(context: Context):LocalDataSource
        {
            if(localDataSource ==null)
            {
                localDataSource = LocalSourceImpl(context)
            }
            return localDataSource!!
        }
    }

    override suspend fun insertLocation(location: LocationData) {
                locationOps.insertLocation(location.location)
                location.days.forEach {
                    locationOps.insertDay(it)
                }
                location.hours.forEach {
                    locationOps.insertHour(it)
                }
    }
    override suspend fun insertAlert(alert: Alert) {
        locationOps.insertAlert(alert)
    }

    override suspend fun deleteLocation(location: LocationData) {
        locationOps.deleteLocation(location.location.address)
        location.days.forEach {
            locationOps.deleteDay(it.getDayId(),location.location.address)
        }
        location.hours.forEach {
            locationOps.deleteHour(it.getId(),location.location.address)
        }
    }

    override suspend fun deleteAlert(id: Int) {
        locationOps.deleteAlert(id)
    }

    override suspend fun getListOfFavLocations(fav: Boolean): Flow<List<Location>> {
      return flow{emit(locationOps.getListOfFavLocations(fav))}
    }

    override suspend fun selectDaysOfLocation(address: String): Flow<List<DayWeather>> {
        return flow{
            emit(locationOps.selectDaysOfLocation(address))
        }
    }

    override suspend fun selectHoursInLocation(address: String): Flow<List<HourWeather>> {
        return flow{
            emit(locationOps.selectHoursInLocation(address))
        }
    }

    override suspend fun getCurrentLocation(isCurrent: Boolean): Flow<Location> {
        return flow{
            emit(locationOps.getCurrentLocation(true))
        }
    }

    override suspend fun getListOfAlerts(): Flow<List<Alert>>{
        return flow{
            emit(locationOps.getListOfAlerts())
        }
    }

}