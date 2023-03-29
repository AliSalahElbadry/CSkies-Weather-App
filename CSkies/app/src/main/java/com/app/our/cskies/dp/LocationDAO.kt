package com.app.our.cskies.dp

import androidx.room.*
import com.app.our.cskies.dp.model.*
import com.app.our.cskies.model.LocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

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

    @Query("DELETE FROM DayWeather  WHERE  address = :address")
    suspend fun deleteDay(address: String)

    @Query("DELETE FROM HourWeather WHERE  address = :address" )
    suspend fun deleteHour(address: String)

    @Query("DELETE FROM Alert WHERE id = :id")
    suspend fun deleteAlert(id:Int)


    //select
    @Query("select * from Location where isFavorite like :fav")
     fun getListOfFavLocations(fav:Boolean):Flow<List<Location>>

    @Query("select * from DayWeather where address like :address")
     fun selectDaysOfLocation(address: String):Flow<List<DayWeather>>

    @Query("select * from HourWeather where  address like :address")
     fun selectHoursInLocation(address:String): Flow<List<HourWeather>>
    @Query("select * from Location where isCurrent like :isCurrent")
    fun getCurrentLocation(isCurrent:Boolean):Flow<Location>

    @Query("select * from Alert")
    fun getListOfAlerts():Flow<List<Alert>>

    @Query("select * from Alert where id in (select id from Alert where address like:address and type like:type)")
    suspend fun getAlert(address: String,type:Int):Alert

    @Query("update Alert set numOfDays =:num where id =:id")
    suspend fun updateAlert(num:Int,id:Int)
}