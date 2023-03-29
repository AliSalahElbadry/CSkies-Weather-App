package com.app.our.cskies.dp

import androidx.room.Query
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocalDataSource {

    suspend fun insertLocation(location: LocationData)
    suspend fun insertAlert(alert: Alert)

    //delete
    suspend fun deleteLocation(location: Location)
    suspend fun deleteAlert(id:Int)

    //select
     fun getListOfFavLocations(fav:Boolean): Flow<List<Location>>

     fun selectDaysOfLocation(address: String):Flow<List<DayWeather>>

     fun selectHoursInLocation(address:String):Flow<List<HourWeather>>

     fun getCurrentLocation(isCurrent:Boolean): Flow<Location>
     fun getListOfAlerts(): Flow<List<Alert>>
    suspend fun getAlert(address: String,type:Int):Alert
    suspend fun  updateAlert(alert: Alert)
}