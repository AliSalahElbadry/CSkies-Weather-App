package com.app.our.cskies.alerts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.utils.UserCurrentLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AlertsViewModel(val repository: Repository) :ViewModel(){
    private var _alertSetter= MutableLiveData<Alert>()
    var alertSetter:LiveData<Alert> =_alertSetter

    private var _alerts= MutableLiveData<List<Alert>>()
    val alerts:LiveData<List<Alert>> = _alerts
    private var newAlert: Alert=Alert("","",false,false,false,false,false,"","","",0)
    fun insertAlert(alert: Alert){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAlert(alert)
            withContext(Dispatchers.Main)
            {
                getAllAlerts()
            }
        }
    }
    fun deleteAlert(alert:Alert)
    {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAlert(alert.getId()!!)
            withContext(Dispatchers.Main)
            {
                getAllAlerts()
            }
        }
    }
     fun getAllAlerts()
    {
        viewModelScope.launch (Dispatchers.IO){
            repository.getListOfAlerts().collect{
                _alerts.postValue(it)
            }
        }
    }
    fun setAlertTypes(temperature:Boolean,windSpeed:Boolean,humidity:Boolean,visibility:Boolean,pressure:Boolean){
        newAlert.pressure=pressure
        newAlert.humidity=humidity
        newAlert.temperature=temperature
        newAlert.windSpeed=windSpeed
        newAlert.visibility=visibility

        newAlert.lat=UserCurrentLocation.favoriteLat
        newAlert.lon=UserCurrentLocation.favoriteLon
    }
    fun setAlertToFrom(toDate:String,fromDate:String,type:Int,address:String,numOfDays:Int){
        Log.e("","Added............viewModel")
        newAlert.fromDate=fromDate
        newAlert.toDate=toDate
        newAlert.type=type
        newAlert.address=address
        newAlert.numOfDays=numOfDays
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                insertAlert(newAlert)
            }.join()
            launch {
                withContext(Dispatchers.Main) {
                    _alertSetter.value = repository.getAlert(newAlert.address, newAlert.type)
                }
            }
        }
    }

}