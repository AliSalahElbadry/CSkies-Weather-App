package com.app.our.cskies.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.widget.ImageView
import com.app.our.cskies.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget

object DataBinding {

    fun getIconFromUrl(context: Context,view:ImageView,url:String){
            Glide.with(context)
            .asBitmap()
            .load(url).into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {

                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }
}