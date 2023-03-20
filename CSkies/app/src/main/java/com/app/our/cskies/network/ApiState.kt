package com.app.our.cskies.network

import com.app.our.cskies.network.model.WeatherLocationData


sealed class ApiState(){
    class Success(val locationData: WeatherLocationData):ApiState()
    class Failure(var message:String):ApiState()
    class Loading(var message:String="LOADING"):ApiState()
}
