package com.app.our.cskies.weather_data_show.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.network.model.WeatherLocationData
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserCurrentLocation
import com.app.our.cskies.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationDataViewModel(val repoClass: Repository):ViewModel() {

   private var _liveData=MutableLiveData<ApiState>(ApiState.Loading())
    val liveData: LiveData<ApiState> = _liveData
    fun getAllFromNetwork() {

        val lat= UserCurrentLocation.latitude?:""
        val lon= UserCurrentLocation.longitude?:""
        if(lat.isNotEmpty()&&lon.isNotEmpty()) {
            val lang = if (Setting.lang == Setting.Lang.EN) "EN" else "AR"
            viewModelScope.launch(Dispatchers.IO) {
                repoClass.getWeatherData(lat, lon, lang = lang).collect {
                    _liveData.postValue(it)
                }
            }
        }
    }
    fun transformToViewData(data: WeatherLocationData)
    {

    }

}