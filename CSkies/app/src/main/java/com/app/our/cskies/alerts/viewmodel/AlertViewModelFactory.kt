package com.app.our.cskies.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.weather_data_show.viewmodel.LocationDataViewModel

class AlertViewModelFactory(val repository: Repository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AlertsViewModel::class.java)){
            AlertsViewModel(repository) as T
        }else{
            throw IllegalArgumentException()
        }
    }
}