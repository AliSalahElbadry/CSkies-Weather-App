package com.app.our.cskies.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(val repoClass:Repository):ViewModel() {

    private var _allFavorites=MutableLiveData<List<Location>>()
    val liveData: LiveData<List<Location>> = _allFavorites
    private var _locationData=MutableLiveData<LocationData>()
    val locationData: LiveData<LocationData> = _locationData
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
        var locationD=LocationData(location, listOf(), listOf())
        viewModelScope.launch(Dispatchers.IO)
        {
            launch {
             withContext(Dispatchers.Default) {
              repoClass.selectDaysOfLocation(location.address)
             }.collect {
                locationD.days = it
            }
             withContext(Dispatchers.Default){
                repoClass.selectHoursInLocation(location.address)
            }.collect {
                locationD.hours=it
            }
            }.join()
            _locationData.postValue(locationD)
        }
    }

    fun deleteLocation(location: Location) {
        val locationData=LocationData(location, listOf(), listOf())
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                launch {
                    repoClass.selectDaysOfLocation(location.address).collect{
                        locationData.days=it
                    }
                }
                launch {
                    repoClass.selectHoursInLocation(location.address).collect{
                        locationData.hours=it
                    }
                }
            }.join()
            launch {
                repoClass.deleteLocation(locationData)
            }
        }
    }

}