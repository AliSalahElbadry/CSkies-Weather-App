package com.app.our.cskies.dp.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DayWeather")
data class DayWeather(
    @ColumnInfo(name = "dayId")
    @PrimaryKey(autoGenerate = true)
    private val dayId:Int,
    val day:String,
    val address:String,
    val maxTemp:Int,
    val minTemp:Int,
    val description:String,
    val icon:String

){
    fun getDayId():Int{
        return dayId
    }
}
