package com.app.our.cskies.dp.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream

@Entity(tableName = "Location")
data class Location(
    val lang:String,
    val date:String,
    @ColumnInfo(name = "address")
    @PrimaryKey(autoGenerate = false)
    var address:String,
    val description:String,
    var isFavorite:Boolean,
    val temp:Int,
    val pressure:Int,
    val humidity:Int,
    val clouds:Int,
    val icon:String,
    var isCurrent:Boolean,
    val windSpeed:Int
    ){

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
