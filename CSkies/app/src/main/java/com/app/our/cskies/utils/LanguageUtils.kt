package com.app.our.cskies.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.View
import java.util.*

object LanguageUtils {
    fun get_En_To_Ar_Numbers(number: String): String {
        val arabicNumber = mutableListOf<String>()
        for (element in number.toString()) {
            when (element) {
                '1' -> arabicNumber.add("١")
                '2' -> arabicNumber.add("٢")
                '3' -> arabicNumber.add("٣")
                '4' -> arabicNumber.add("٤")
                '5' -> arabicNumber.add("٥")
                '6' -> arabicNumber.add("٦")
                '7' -> arabicNumber.add("٧")
                '8' -> arabicNumber.add("٨")
                '9' -> arabicNumber.add("٩")
                '0' ->arabicNumber.add("٠")
                '.'->arabicNumber.add(".")
                '-'->arabicNumber.add("-")
                else -> arabicNumber.add(".")
            }
        }
        return arabicNumber.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
            .replace(" ", "")
    }
    fun getAddressEnglish(context: Context, lat: Double?, lon: Double?):String
    {
        var address:MutableList<Address>?=null
        val geocoder= Geocoder(context)
        address =geocoder.getFromLocation(lat!!,lon!!,1)
        if (address?.isEmpty()==true)
        {
            return "Unkown location"
        }
        else if (address?.get(0)?.countryName.isNullOrEmpty())
        {
            return "Unkown Country"
        } else if (address?.get(0)?.adminArea.isNullOrEmpty())
        {
            return address?.get(0)?.countryName.toString()
        }
        else return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }
    fun getAddressArabic(context: Context, lat:Double, lon:Double):String
    {
        var address:MutableList<Address>?=null
        val geocoder= Geocoder(context, Locale("ar"))
        address =geocoder.getFromLocation(lat,lon,1)
        if (address?.isEmpty()==true)
        {
            return "Unkown location"
        }
        else if (address?.get(0)?.countryName.isNullOrEmpty())
        {
            return "Unkown Country"
        }
        else if (address?.get(0)?.adminArea.isNullOrEmpty())
        {
            return address?.get(0)?.countryName.toString()
        }
        else return address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
    }

    fun setViewLanguage(view: View, lang:Setting.Lang) {
        val languageToLoad =if(lang==Setting.Lang.AR)"ar" else "en"
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        view.context.resources.updateConfiguration(
            config,
            view.context.resources.displayMetrics
        )
    }
    fun updateContextLanguage(context: Context, language: String): Boolean {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return true
    }

    @SuppressLint("ObsoleteSdkInt")
    fun setAppLocale(localeCode: String, context: Context) {
        val resources = context.resources
        val dm = resources.displayMetrics
        val config: Configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            config.setLocale(Locale(localeCode.lowercase(Locale.getDefault())))
        }
        else
        {
            config.locale = Locale(localeCode.lowercase(Locale.getDefault()))
        }
        resources.updateConfiguration(config, dm)
    }
    @SuppressLint("ObsoleteSdkInt")
    fun changeLang(context: Context, lang_code: String): ContextWrapper {
        var myContext=context
        val sysLocale: Locale
        val rs: Resources = context.resources
        val config: Configuration = rs.configuration
        sysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales.get(0)
        } else {
            config.locale
        }
        if (lang_code != "" && !sysLocale.language.equals(lang_code)) {
            val locale = Locale(lang_code)
            Locale.setDefault(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            { config.setLocale(locale) }
            else { config.locale = locale }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                myContext = context.createConfigurationContext(config)
            } else {
                myContext.resources.updateConfiguration(config, context.resources.displayMetrics)
            }
        }
        return ContextWrapper(myContext)
    }
}