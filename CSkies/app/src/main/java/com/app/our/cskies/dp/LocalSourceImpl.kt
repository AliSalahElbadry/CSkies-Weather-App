package com.app.our.cskies.dp

import android.content.Context
import android.util.Log
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
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
                    it.address=location.location.address
                    Log.e("",it.address)
                    locationOps.insertDay(it)
                }
                location.hours.forEach {
                    it.address=location.location.address
                    locationOps.insertHour(it)
                }
    }
    override suspend fun insertAlert(alert: Alert) {
        locationOps.insertAlert(alert)
    }

    override suspend fun deleteLocation(location: Location) {
            locationOps.deleteLocation(location.address)
            locationOps.deleteDay(location.address)
            locationOps.deleteHour(location.address)
    }

    override suspend fun deleteAlert(id: Int) {
        locationOps.deleteAlert(id)
    }

    override  fun getListOfFavLocations(fav: Boolean): Flow<List<Location>> {
      return locationOps.getListOfFavLocations(fav)
    }

    override  fun selectDaysOfLocation(address: String): Flow<List<DayWeather>> {
        return locationOps.selectDaysOfLocation(address)
    }

    override  fun selectHoursInLocation(address: String): Flow<List<HourWeather>> {
        return locationOps.selectHoursInLocation(address)
    }

    override  fun getCurrentLocation(isCurrent: Boolean): Flow<Location> {
        return locationOps.getCurrentLocation(true)
    }

    override  fun getListOfAlerts(): Flow<List<Alert>>{
        return locationOps.getListOfAlerts()
    }

    override suspend fun getAlert(address: String, type: Int): Alert {
       return locationOps.getAlert(address,type)
    }

    override suspend fun updateAlert(alert: Alert) {
        locationOps.updateAlert(alert.numOfDays,alert.getId()!!)
    }
}