package com.app.our.cskies.weather_data_show.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.Repository.RepositoryInterface

class LocationDataViewModelFactory (private val myRepo: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(LocationDataViewModel::class.java)){
            LocationDataViewModel(myRepo) as T
        }else{
            throw IllegalArgumentException()
        }
    }
}