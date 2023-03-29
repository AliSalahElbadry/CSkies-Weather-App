package com.app.our.cskies.favorites.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.home.viewModel.ViewModelHome
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.ApiState
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserCurrentLocation
import com.app.our.cskies.weather_data_show.viewmodel.FactoryTransformer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(private val repoClass:Repository):ViewModel() {

    private var _allFavorites=MutableLiveData<List<Location>>()
    val liveData: LiveData<List<Location>> = _allFavorites

    private var _locationData=MutableLiveData<LocationData>()
    val locationData: LiveData<LocationData> = _locationData

    private var _location=MutableLiveData<Location>()
    val newLocation: LiveData<Location> = _location
    fun getAllFavoriteLocations()
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repoClass.getListOfFavLocations(true).collect {
                _allFavorites.postValue(it)
            }
        }
    }

    fun getLocationData(location: Location) {
        viewModelScope.launch(Dispatchers.IO)
        {
               val locationD=LocationData(location, listOf(), listOf())
                repoClass.selectHoursInLocation(location.address).collect {
                    locationD.hours = it
                    repoClass.selectDaysOfLocation(location.address).collect{days->
                        locationD.days=days
                        _locationData.postValue(locationD)
                    }
                }
        }
    }

    fun deleteLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
                repoClass.deleteLocation(location)
        }
    }

    fun setUpFavoriteLocation(context: Context) {
        val lat= UserCurrentLocation.latitude?:""
        val lon= UserCurrentLocation.longitude?:""
        if(lat.isNotEmpty()&&lon.isNotEmpty()) {
            val lang = Setting.getLang()
            viewModelScope.launch(Dispatchers.IO) {
                repoClass.getWeatherData(lat, lon, lang = lang).collect {
                    when(it) {
                        is ApiState.Success->  {
                            val factory= FactoryTransformer(context)
                            factory.mapWeatherNetworkToWeatherApp(it.locationData,
                                isCurrent = false,
                                isFavorite = true
                            )
                            factory.stateFlow.collect{num->
                                if(num==33)
                                {
                                    val data=factory.getLocationData()
                                    _location.postValue(data.location)
                                    insertLocation(data)
                                }
                            }
                        }
                        is ApiState.Failure-> {
                            Log.i("","Error Adding New Location")
                        }
                        else ->{

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

    fun setTitle(i: Int) {
        ViewModelHome().setTitle(i)
    }

}