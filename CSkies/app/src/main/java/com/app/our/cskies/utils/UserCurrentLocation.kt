package com.app.our.cskies.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.*

object UserCurrentLocation {
    var latitude:String?=null
    var longitude:String?=null
    var exclude:String="weekly"
    var units:String="metric"
    var language:String="en"
    private fun getAddressEnglish(context: Context):String
    {
        if(latitude.isNullOrEmpty()|| longitude.isNullOrEmpty())return "Unknown location"
        var address:MutableList<Address>?=null
        val geocoder= Geocoder(context,Locale("en"))
        address =geocoder.getFromLocation(latitude!!.toDouble(), longitude!!.toDouble(),1)
        return if (address?.isEmpty()==true) {
            "Unknown location"
        } else if (address?.get(0)?.countryName.isNullOrEmpty()) {
            "Unknown Country"
        } else if (address?.get(0)?.adminArea.isNullOrEmpty()) {
            address?.get(0)?.countryName.toString()
        } else address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }
   private fun getAddressArabic(context: Context):String
    {
        if(latitude.isNullOrEmpty()|| longitude.isNullOrEmpty())return "Unkown location"
        var address:MutableList<Address>?=null
        val geocoder= Geocoder(context, Locale("ar"))
        address =geocoder.getFromLocation(latitude!!.toDouble(), longitude!!.toDouble(),1)
        return if (address?.isEmpty()==true) {
            "Unkown location"
        } else if (address?.get(0)?.countryName.isNullOrEmpty()) {
            "Unkown Country"
        } else if (address?.get(0)?.adminArea.isNullOrEmpty()) {
            address?.get(0)?.countryName.toString()
        } else address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }
    fun getAddress(context: Context):String{
        return if(Setting.getLang()=="en")
        {
         getAddressEnglish(context)
        }else{
            getAddressArabic(context)
        }
    }
}