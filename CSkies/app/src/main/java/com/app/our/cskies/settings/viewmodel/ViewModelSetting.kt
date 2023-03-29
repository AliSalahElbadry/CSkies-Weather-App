package com.app.our.cskies.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.app.our.cskies.home.viewModel.ViewModelHome

class ViewModelSetting : ViewModel(){

    fun setTitle(i:Int){
        ViewModelHome().setTitle(i)
    }
}