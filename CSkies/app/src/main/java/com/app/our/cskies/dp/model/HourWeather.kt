package com.app.our.cskies.dp.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream

@Entity(tableName = "HourWeather")
data class HourWeather(
    val address:String,
    val hour:String,
    val temp:Int,
    val icon:String
){
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private var id:Int=0
    fun getId():Int{
        return id
    }
    fun setId(id:Int){
        this.id=id
    }

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private var image: ByteArray?=null
    fun getImageBitmap(): Bitmap? {
        return if (image == null) null else BitmapFactory.decodeByteArray(image, 0, image!!.size)
    }
    fun setImageBitmap(image: Bitmap?) {
        if (image != null) {
            val stream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, stream)
            this.image = stream.toByteArray()
        }
    }
    fun getImage(): ByteArray? {
        return image
    }

    fun setImage(image: ByteArray?) {
        this.image = image
    }
}
