package com.app.our.cskies.dp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HourWeather")
data class HourWeather(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
   private val id:Int,
    val address:String,
    val hour:String,
    val temp:Int,
    val icon:String
){
    fun getId():Int{
        return id
    }
}
