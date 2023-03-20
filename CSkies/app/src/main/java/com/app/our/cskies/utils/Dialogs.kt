package com.app.our.cskies.utils

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar


object Dialogs {

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
    fun getProgressDialog(context: Context, title:String, message:String): ProgressDialog {
        var progressDialog = ProgressDialog(context)
        progressDialog.setTitle(title)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false) // blocks UI interaction
        return progressDialog
    }

}