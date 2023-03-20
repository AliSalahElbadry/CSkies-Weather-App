package com.app.our.cskies.dp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Alert")
data class Alert(
      @ColumnInfo(name = "id")
      @PrimaryKey(autoGenerate = true)
      private val id:Int,
                 val fromDate:String,
                 val toDate:String,
                 val address:String,
                 val lat:String,
                 val lon:String){
    fun getId():Int{
        return id
    }
}
