package com.app.our.cskies.network

import com.app.our.cskies.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit  {
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
           val myService: ServiceApi = retrofit.create(ServiceApi::class.java)
}