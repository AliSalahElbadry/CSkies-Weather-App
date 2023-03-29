package com.app.our.cskies.dp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Alert")
data class Alert(
    var fromDate:String,
    var toDate:String,
    var temperature:Boolean,
    var humidity:Boolean,
    var visibility:Boolean,
    var windSpeed: Boolean,
    var pressure:Boolean,
    var address:String,
    var lat:String,
    var lon:String,var numOfDays:Int) :java.io.Serializable{
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private var id:Int?=null
    var type:Int=0
    fun getId():Int?{
        return id
    }
    fun setId(id:Int){
        this.id=id
    }
}
