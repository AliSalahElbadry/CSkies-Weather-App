package com.app.our.cskies.weather_data_show.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserCurrentLocation
import com.app.our.cskies.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationDataViewModel(private val repoClass: Repository):ViewModel() {

   private var _stateFlow= MutableStateFlow<ApiState>(ApiState.Loading())
    val stateFlow: StateFlow<ApiState> = _stateFlow
    fun getAllFromNetwork(context:Context,isCurrent:Boolean,isFavorite:Boolean) {
        val lat= UserCurrentLocation.latitude?:""
        val lon= UserCurrentLocation.longitude?:""
        if(lat.isNotEmpty()&&lon.isNotEmpty()) {
            val lang =Setting.getLang()
            viewModelScope.launch(Dispatchers.IO) {
                repoClass.getWeatherData(lat, lon, lang = lang).collect {
                    when(it) {
                       is ApiState.Success->  {
                       val factory= FactoryTransformer(context)
                           factory.mapWeatherNetworkToWeatherApp(it.locationData,isCurrent,isFavorite)
                           factory.stateFlow.collect{num->
                               if(num==33)
                               {
                                   val data=factory.getLocationData()
                                   withContext(Dispatchers.Main) {
                                       _stateFlow.value = ApiState.TransformedState(data)
                                   }
                                   insertLocation(data)
                               }
                           }
                      }
                      is ApiState.Failure-> {
                          withContext(Dispatchers.Main) {
                              _stateFlow.value = it
                          }
                      }
                      else ->{
                          withContext(Dispatchers.Main) {
                              _stateFlow.value = it
                          }
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
    fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            repoClass.getCurrentLocation(true).collect{
                var locationData=LocationData(it, listOf(), listOf())
                repoClass.selectHoursInLocation(it.address).collect{hours->
                    locationData.hours=hours
                    repoClass.selectDaysOfLocation(it.address).collect{days->
                        locationData.days=days
                        withContext(Dispatchers.Main) {
                            _stateFlow.value = ApiState.TransformedState(locationData)
                        }
                    }
                }
            }
        }
    }
}