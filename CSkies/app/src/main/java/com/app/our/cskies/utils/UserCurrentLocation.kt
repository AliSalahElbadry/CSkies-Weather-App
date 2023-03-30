package com.app.our.cskies.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.*

object UserCurrentLocation {
    var latitude:String?=null
    var longitude:String?=null
    var exclude:String="weekly"
    var units:String="metric"
    var language:String="en"
    var favoriteLat:String=""
    var favoriteLon:String=""
    fun getAddress(context: Context,isFavorite:Boolean):String
    {
        var address:MutableList<Address>?=null
        val geocoder= Geocoder(context,Locale(Setting.getLang()))
         if(!isFavorite&& !latitude.isNullOrEmpty())
             address =  geocoder.getFromLocation(latitude!!.toDouble(), longitude!!.toDouble(),1)
        else if(favoriteLat.isNotEmpty())
             address = geocoder.getFromLocation(favoriteLat.toDouble(), favoriteLon.toDouble(),1)
        return if (address?.isEmpty()==true) {
            "Unknown location"
        } else if (address?.get(0)?.countryName.isNullOrEmpty()) {
            "Unknown Country"
        } else if (address?.get(0)?.adminArea.isNullOrEmpty()) {
            address?.get(0)?.countryName.toString()
        } else address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }
    fun getLatLonForAddress(location: String, context: Context): LatLng {
        val geocoder = Geocoder(context, Locale(Setting.getLang()))
        val addressList = geocoder.getFromLocationName(location, 1)
        var address:Address?=null
        if(addressList?.isNotEmpty() == true)
        {
            address = addressList[0]
        }

        return if(address!=null)LatLng(address.latitude, address.longitude) else LatLng(0.0, 0.0)
    }
}