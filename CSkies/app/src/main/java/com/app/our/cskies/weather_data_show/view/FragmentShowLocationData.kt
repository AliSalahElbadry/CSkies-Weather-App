package com.app.our.cskies.weather_data_show.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.app.our.cskies.R
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.databinding.FragmentShowLocationDataBinding
import com.app.our.cskies.dp.AppDataBase
import com.app.our.cskies.dp.LocalSourceImpl
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.ApiState
import com.app.our.cskies.network.RemoteSourceImpl
import com.app.our.cskies.utils.*
import com.app.our.cskies.weather_data_show.viewmodel.LocationDataViewModel
import com.app.our.cskies.weather_data_show.viewmodel.LocationDataViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentShowLocationData : Fragment() {
    var mode:Int=0
    lateinit var locationDataViewModel: LocationDataViewModel
    lateinit var factory:LocationDataViewModelFactory
    lateinit var binding:FragmentShowLocationDataBinding
    lateinit var daysAdapter: DaysAdapter
    private lateinit var hoursAdapter: HoursAdapter
    lateinit var data: LocationData
    var isFavoriteLocation:Boolean=false
    var isCurrentLocation:Boolean=true
    lateinit var loadingAnim:LottieAnimationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         LanguageUtils.setAppLocale(Setting.getLang(),requireContext())
         binding= FragmentShowLocationDataBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingAnim=view.findViewById(R.id.loadingAnimHome)
        binding.pullToRefresh.visibility=View.INVISIBLE
        factory = LocationDataViewModelFactory(Repository.getInstance(
            LocalSourceImpl.getInstance(AppDataBase.getInstance(requireActivity().applicationContext).getLocationDao()),
            RemoteSourceImpl.getInstance()!!))

        locationDataViewModel=ViewModelProvider(this, factory)[LocationDataViewModel::class.java]

        daysAdapter = DaysAdapter(listOf())
        hoursAdapter = HoursAdapter(listOf())
        binding.recyclerViewdayHoursData.layoutManager =
            LinearLayoutManager(view.context).apply {
                this.orientation = LinearLayoutManager.HORIZONTAL
            }
        binding.pullToRefresh.setOnRefreshListener {
            if(mode==0) {
                if (UserStates.checkConnectionState(requireActivity()))
                    locationDataViewModel.getAllFromNetwork(
                        requireContext(),
                        true,
                        isFavorite = false
                    )
                else {
                    locationDataViewModel.getCurrentLocation()
                }
            }else{
                binding.pullToRefresh.isRefreshing=false
            }
        }
        binding.recyclerViewdayHoursData.adapter = hoursAdapter
        binding.recyclerViewDaysInWeekData.layoutManager = LinearLayoutManager(view.context)
        binding.recyclerViewDaysInWeekData.adapter = daysAdapter
        when (mode) {
            0 -> {
                if(UserStates.checkConnectionState(requireActivity())) {
                    try {
                        locationDataViewModel.getAllFromNetwork(
                            requireContext(),
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
                   lifecycleScope.launch {
                       locationDataViewModel.stateFlow.collect {
                           if (it is ApiState.TransformedState){
                                   loadingAnim.cancelAnimation()
                                   loadingAnim.visibility = View.GONE
                                   binding.pullToRefresh.visibility = View.VISIBLE
                                   binding.pullToRefresh.isRefreshing = false
                                   data = it.locationData
                                   hoursAdapter.hours = data.hours
                                   daysAdapter.daily = data.days
                                   hoursAdapter.notifyDataSetChanged()
                                   daysAdapter.notifyDataSetChanged()
                                   showData()
                               }else if(it  is ApiState.Failure)  {

                                   Toast.makeText(requireContext(), it.message,Toast.LENGTH_LONG).show()
                               }
                               else if(it is ApiState.Loading) {
                                  loadingAnim.playAnimation()
                               }
                           }
                       }
            }
            2 -> {
                //favorite item
                loadingAnim.cancelAnimation()
                loadingAnim.visibility=View.GONE
                binding.pullToRefresh.visibility=View.VISIBLE
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