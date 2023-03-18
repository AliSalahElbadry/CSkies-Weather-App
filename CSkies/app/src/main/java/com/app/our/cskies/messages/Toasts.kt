package com.app.our.cskies.messages

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar


object Toasts {

    fun SnakeToast(view: View,message:String){
        val snackbar = Snackbar.make(view, message,
            Snackbar.LENGTH_SHORT).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.BLACK)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 28f
        textView.textAlignment=TextView.TEXT_ALIGNMENT_CENTER
        snackbar.show()

    }

}