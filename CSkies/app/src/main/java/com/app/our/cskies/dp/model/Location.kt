package com.app.our.cskies.dp.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Location")
data class Location(
    val lang:String,
    val date:String,
    @ColumnInfo(name = "address")
    @PrimaryKey(autoGenerate = false)
    val address:String,
    val description:String,
    val isFavorite:Boolean,
    val temp:Int,
    val pressure:Int,
    val humidity:Int,
    val clouds:Int,
    val icon:String,
    val isCurrent:Boolean
    )
