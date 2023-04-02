package com.app.our.cskies.Repository

import com.app.our.cskies.dp.LocalDataSource
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

class FakeLocalSource(
    var locations:MutableList<Location>,
    var alldays:MutableList<DayWeather>,
    var allHours:MutableList<HourWeather>,
    var alerts:MutableList<Alert>):LocalDataSource {


    override suspend fun insertLocation(location: LocationData) {
        locations.add(location.location)
        location.days.forEach{
            alldays.add(it)
        }
        location.hours.forEach{
            allHours.add(it)
        }
    }

    override suspend fun insertAlert(alert: Alert) {
        alerts.add(alert)
    }

    override suspend fun deleteLocation(location: Location) {
        locations.removeIf {
            it.address==location.address
        }
    }

    override suspend fun deleteAlert(id: Int) {
        alerts.forEach {
            if(it.getId()==id)
            {
                alerts.remove(it)
            }
        }
    }

    override fun getListOfFavLocations(fav: Boolean): Flow<List<Location>> {
        var favLocations= mutableListOf<Location>()
        locations.forEach {
            if(it.isFavorite)
            {
                favLocations.add(it)
            }
        }
        return  flow{
            emit(favLocations)
        }
    }

    override fun selectDaysOfLocation(address: String): Flow<List<DayWeather>> {
       var days= mutableListOf<DayWeather>()
        alldays.forEach {
            if(it.address==address)
            {
                days.add(it)
            }
        }
        return flow{
            emit(days)
        }
    }

    override fun selectHoursInLocation(address: String): Flow<List<HourWeather>> {
       var hours= mutableListOf<HourWeather>()
        allHours.forEach {
            if(it.address==address)
            {
                hours.add(it)
            }
        }
        return flow{
            emit(hours)
        }
    }

    override fun getCurrentLocation(isCurrent: Boolean): Flow<Location> {
        var location=Location("","","","",false,0,0,0,0,"",false,0)
        locations.forEach {
            if(it.isCurrent==isCurrent)
            {
                location=it
            }
        }
        return  flow{
            emit(location)
        }
    }

    override fun getListOfAlerts(): Flow<List<Alert>> {
        return flow {
            emit(alerts)
        }
    }

    override suspend fun getAlert(address: String, type: Int): Alert {
        var alert=Alert("","",false,false,false,false,false,"","","",0)
        alerts.forEach {
            if(it.address==address&&it.type==type)
                alert=it
        }
        return alert
    }

    override suspend fun updateAlert(alert: Alert) {
       alerts.removeIf {
           it.address==alert.address
       }
       alerts.add(alert)
    }
}