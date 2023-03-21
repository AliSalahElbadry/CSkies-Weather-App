package com.app.our.cskies.weather_data_show.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.databinding.FragmentShowLocationDataBinding
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.ApiState
import com.app.our.cskies.utils.*
import com.app.our.cskies.weather_data_show.viewmodel.LocationDataViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


class FragmentShowLocationData : Fragment(),NumloadedImages {
    var mode:Int=0
    lateinit var locationDataViewModel: LocationDataViewModel
    lateinit var binding:FragmentShowLocationDataBinding
    lateinit var daysAdapter: DaysAdapter
    private lateinit var hoursAdapter: HoursAdapter
    lateinit var data: LocationData
    var numOfLoadedImages:Int=0
    var isFavoriteLocation:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         LanguageUtils.setAppLocale(Setting.getLang(),context!!)
         binding= FragmentShowLocationDataBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        daysAdapter = DaysAdapter(listOf(), this)
        hoursAdapter = HoursAdapter(listOf(), this)
        binding.recyclerViewdayHoursData.layoutManager =
            LinearLayoutManager(view.context).apply {
                this.orientation = LinearLayoutManager.HORIZONTAL
            }
        binding.recyclerViewdayHoursData.adapter = hoursAdapter
        binding.recyclerViewDaysInWeekData.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerViewDaysInWeekData.adapter = daysAdapter
        if(mode==0) {
            //connection stable
            locationDataViewModel = LocationDataViewModel(Repository.getInstance(requireContext()))
            if (UserStates.checkConnectionState(requireActivity())) {
                locationDataViewModel.liveData.observe(this) {
                    when (it) {
                        is ApiState.TransformedState -> {
                            data = it.locationData
                            hoursAdapter.hours = data.hours
                            daysAdapter.daily = data.days
                            hoursAdapter.notifyDataSetChanged()
                            daysAdapter.notifyDataSetChanged()
                            showData()
                        }
                        is ApiState.Failure -> {
                            // Toasts.SnakeToast(view,it.message)
                        }
                        else -> {
                            // Toasts.SnakeToast(view,(it as ApiState.Loading).message)
                        }
                    }
                }
            } else {

            }
        }
        else if(mode==1)
        {
         //connection lost
            getLocationDataFromDataBase()
        }else if(mode==2)
        {
           //favorite item
            showFavoriteLocationData()
        }
    }

    private fun showFavoriteLocationData() {

        binding.textViewCity.text=data.location.address
        binding.textViewDayTime.text=data.location.date
        binding.textViewCityTempreture.text="${Converter.getTemperature(data.location.temp)}${Constants.DEGREE_CHAR}${Setting.getTemp()}"
        binding.textViewClouds.text=data.location.clouds.toString()
        binding.textViewWindSpeed.text=Converter.getWindSpeed(data.location.windSpeed)
        binding.textViewDescription.text=data.location.description
        binding.textViewPressure.text=data.location.pressure.toString()
        binding.textViewHumidity.text=data.location.humidity.toString()
        hoursAdapter.hours = data.hours
        daysAdapter.daily = data.days
        hoursAdapter.notifyDataSetChanged()
        daysAdapter.notifyDataSetChanged()
    }
    fun getLocationDataFromDataBase(){

    }

    private fun showData()
    {
        Glide.with(binding.root.context)
            .asBitmap()
            .load(data.location.icon)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    setNumLoadedImages(1)
                    data.location.setImageBitmap(resource)
                    binding.imageViewWeatherIcon.setImageBitmap(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
        data.location.address=UserCurrentLocation.getAddress(requireContext())
        binding.textViewCity.text=data.location.address
        binding.textViewDayTime.text=data.location.date
        binding.textViewCityTempreture.text="${Converter.getTemperature(data.location.temp)}${Constants.DEGREE_CHAR}${Setting.getTemp()}"
        binding.textViewClouds.text=data.location.clouds.toString()
        binding.textViewWindSpeed.text=Converter.getWindSpeed(data.location.windSpeed)
        binding.textViewDescription.text=data.location.description
        binding.textViewPressure.text=data.location.pressure.toString()
        binding.textViewHumidity.text=data.location.humidity.toString()
        hoursAdapter.hours = data.hours
        daysAdapter.daily = data.days
        hoursAdapter.notifyDataSetChanged()
        daysAdapter.notifyDataSetChanged()

    }
    override fun onResume() {
        super.onResume()
        if(mode==0) {
            locationDataViewModel.getAllFromNetwork()
        }
    }

    override fun setNumLoadedImages(num: Int) {
        numOfLoadedImages++
        Log.e("","$numOfLoadedImages")
        if(numOfLoadedImages==33)
        {
            if(isFavoriteLocation)
            {
                data.location.isFavorite=true

            }else{
                data.location.isCurrent=true
            }
            data.days=daysAdapter.daily
            data.hours=hoursAdapter.hours
            //locationDataViewModel.insertLocation(data)
        }
    }

    override fun getMyMode(): Int {
       return  mode
    }

}