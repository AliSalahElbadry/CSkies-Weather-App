package com.app.our.cskies.weather_data_show.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import com.app.our.cskies.Repository.FactoryTransformer
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.model.WeatherLocationData
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserCurrentLocation
import com.app.our.cskies.network.ApiState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationDataViewModel(val repoClass: Repository):ViewModel() {

   private var _liveData=MutableLiveData<ApiState>()
    val liveData: LiveData<ApiState> = _liveData
    fun getAllFromNetwork() {

        val lat= UserCurrentLocation.latitude?:""
        val lon= UserCurrentLocation.longitude?:""
        if(lat.isNotEmpty()&&lon.isNotEmpty()) {
            val lang =Setting.getLang()
            viewModelScope.launch(Dispatchers.IO) {
                repoClass.getWeatherData(lat, lon, lang = lang).collect {
                    when(it) {
                       is ApiState.Success->  {
                        _liveData.postValue(ApiState.TransformedState(FactoryTransformer().mapWeatherNetworkToWeatherApp(it.locationData)))
                      }
                      is ApiState.Failure-> {
                          _liveData.postValue(it)
                      }
                      else ->{
                          _liveData.postValue(it)
                      }
                    }
                }
            }
        }
    }
    fun insertLocation(data: LocationData) {
        viewModelScope.launch(Dispatchers.IO) {
            repoClass.insertLocation(data)
        }
    }
}