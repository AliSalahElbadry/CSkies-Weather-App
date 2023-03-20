package com.app.our.cskies.dp

import androidx.room.Query
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun insertLocation(location: LocationData)
    suspend fun insertAlert(alert: Alert)

    //delete
    suspend fun deleteLocation(location: LocationData)
    suspend fun deleteAlert(id:Int)

    //select
    suspend fun getListOfFavLocations(fav:Boolean): Flow<List<Location>>

    suspend fun selectDaysOfLocation(address: String):Flow<List<DayWeather>>

    suspend fun selectHoursInLocation(address:String):Flow<List<HourWeather>>

    suspend fun getCurrentLocation(isCurrent:Boolean):Flow<Location>
    suspend fun getListOfAlerts(): Flow<List<Alert>>
}