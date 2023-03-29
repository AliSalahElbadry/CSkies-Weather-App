package com.app.our.cskies.weather_data_show.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.our.cskies.R
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.databinding.FragmentShowLocationDataBinding
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.ApiState
import com.app.our.cskies.utils.*
import com.app.our.cskies.weather_data_show.viewmodel.LocationDataViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


class FragmentShowLocationData : Fragment() {
    var mode:Int=0
    lateinit var locationDataViewModel: LocationDataViewModel
    lateinit var binding:FragmentShowLocationDataBinding
    lateinit var daysAdapter: DaysAdapter
    private lateinit var hoursAdapter: HoursAdapter
    lateinit var data: LocationData
    var isFavoriteLocation:Boolean=false
    var isCurrentLocation:Boolean=true
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
        locationDataViewModel = LocationDataViewModel(Repository.getInstance(requireContext()))
        daysAdapter = DaysAdapter(listOf())
        hoursAdapter = HoursAdapter(listOf())
        binding.recyclerViewdayHoursData.layoutManager =
            LinearLayoutManager(view.context).apply {
                this.orientation = LinearLayoutManager.HORIZONTAL
            }
        binding.pullToRefresh.setOnRefreshListener {
            if(UserStates.checkConnectionState(requireActivity()))
            locationDataViewModel.getAllFromNetwork(requireContext(),true, isFavorite = false)
            else{
                locationDataViewModel.getCurrentLocation()
            }
        }
        binding.recyclerViewdayHoursData.adapter = hoursAdapter
        binding.recyclerViewDaysInWeekData.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerViewDaysInWeekData.adapter = daysAdapter
        when (mode) {
            0 -> {
                requireActivity().title=resources.getString(R.string.home)
                if(UserStates.checkConnectionState(requireActivity())) {
                    try {
                        locationDataViewModel.getAllFromNetwork(
                            this.context!!,
                            isCurrentLocation,
                            isFavoriteLocation
                        )
                    }catch (e:Exception){
                        Dialogs.SnakeToast(binding.root,if(Setting.getLang()=="en")"Network Error" else "مشكلة في الاتصال ")
                        Log.e("","Network Error")
                    }
                }else{
                    locationDataViewModel.getCurrentLocation()
                }
                    locationDataViewModel.liveData.observe(this) {
                        when (it) {
                            is ApiState.TransformedState -> {
                                binding.pullToRefresh.isRefreshing = false
                                data = it.locationData
                                hoursAdapter.hours = data.hours
                                daysAdapter.daily = data.days
                                hoursAdapter.notifyDataSetChanged()
                                daysAdapter.notifyDataSetChanged()
                                showData()
                            }
                            is ApiState.Failure -> {

                                Dialogs.SnakeToast(binding.root, it.message)
                            }
                            else -> {
                                Dialogs.SnakeToast(binding.root, (it as ApiState.Loading).message)
                            }
                        }
                    }

            }
            2 -> {
                //favorite item
                showFavoriteLocationData()
            }
        }
    }

    private fun showFavoriteLocationData() {

        binding.textViewCity.text=data.location.address
        binding.textViewDayTime.text=data.location.date
        binding.imageViewWeatherIcon.setImageBitmap(data.location.getImageBitmap())
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


    @SuppressLint("SetTextI18n")
    private fun showData()
    {
        binding.imageViewWeatherIcon.setImageBitmap(data.location.getImageBitmap())
        binding.textViewCity.text=data.location.address
        binding.textViewDayTime.text=data.location.date
        binding.textViewCityTempreture.text=
             if(Setting.getLang()=="ar")
            "${LanguageUtils.get_En_To_Ar_Numbers(Converter.getTemperature(data.location.temp).toString())}${Constants.DEGREE_CHAR}${Setting.getTemp()}"
             else
                 "${Converter.getTemperature(data.location.temp)}${Constants.DEGREE_CHAR}${Setting.getTemp()}"
      if(Setting.getLang()=="en") {
          binding.textViewClouds.text = data.location.clouds.toString()+" %"
          binding.textViewWindSpeed.text = Converter.getWindSpeed(data.location.windSpeed)
          binding.textViewDescription.text = data.location.description
          binding.textViewPressure.text = data.location.pressure.toString()+" hpa"
          binding.textViewHumidity.text = data.location.humidity.toString()+" %"
      }else{
          binding.textViewClouds.text = LanguageUtils.get_En_To_Ar_Numbers(data.location.clouds.toString())+" %"
          binding.textViewWindSpeed.text = LanguageUtils.get_En_To_Ar_Numbers(Converter.getWindSpeed(data.location.windSpeed))+Setting.getWSpeed()
          binding.textViewDescription.text = data.location.description
          binding.textViewPressure.text = LanguageUtils.get_En_To_Ar_Numbers(data.location.pressure.toString())+ " هيكتوباسكال "
          binding.textViewHumidity.text = LanguageUtils.get_En_To_Ar_Numbers(data.location.humidity.toString())+" %"
      }
    }
    override fun onResume() {
        super.onResume()
    }
}