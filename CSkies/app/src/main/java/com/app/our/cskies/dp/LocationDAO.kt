package com.app.our.cskies.dp

import androidx.room.*
import com.app.our.cskies.dp.model.*
import com.app.our.cskies.model.LocationData
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDAO {

    //insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(dayWeather: DayWeather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHour(hourWeather: HourWeather)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert)

    //delete
    @Query("DELETE FROM Location WHERE address = :address")
    suspend fun deleteLocation(address:String)

    @Query("DELETE FROM DayWeather WHERE dayId = :dayId and address = :address")
    suspend fun deleteDay(dayId: Int,address: String)

    @Query("DELETE FROM HourWeather WHERE id = :hourId And address like :address" )
    suspend fun deleteHour(hourId:Int,address: String)

    @Query("DELETE FROM Alert WHERE id = :id")
    suspend fun deleteAlert(id:Int)


    //select
    @Query("select * from Location where isFavorite like :fav")
    suspend fun getListOfFavLocations(fav:Boolean):List<Location>

    @Query("select * from DayWeather where address like :address")
    suspend fun selectDaysOfLocation(address: String):List<DayWeather>

    @Query("select * from HourWeather where  address like :address")
    suspend fun selectHoursInLocation(address:String): List<HourWeather>
    @Query("select * from Location where isCurrent like :isCurrent")
    suspend fun getCurrentLocation(isCurrent:Boolean):Location

    @Query("select * from Alert")
    suspend fun getListOfAlerts():List<Alert>

}