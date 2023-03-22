package com.app.our.cskies.dp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location

@Database(entities = [Location::class,DayWeather::class,HourWeather::class, Alert::class], version =1 )
abstract class AppDataBase: RoomDatabase() {
    abstract fun getLocationDao(): LocationDAO
    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null
        fun getInstance(ctx: Context): AppDataBase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(ctx.applicationContext, AppDataBase::class.java, "locations_database").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}