package com.app.our.cskies.weather_data_show.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.databinding.FragmentShowLocationDataBinding
import com.app.our.cskies.network.model.WeatherLocationData
import com.app.our.cskies.network.ApiState
import com.app.our.cskies.utils.*
import com.app.our.cskies.weather_data_show.viewmodel.LocationDataViewModel
import com.bumptech.glide.Glide


class FragmentShowLocationData : Fragment() {
    lateinit var locationDataViewModel: LocationDataViewModel
    lateinit var binding:FragmentShowLocationDataBinding
    lateinit var daysAdapter: DaysAdapter
    private lateinit var hoursAdapter: HoursAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding= FragmentShowLocationDataBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationDataViewModel = LocationDataViewModel(Repository.getInstance(requireContext()))
        daysAdapter= DaysAdapter(listOf())
        hoursAdapter= HoursAdapter(listOf())
        binding.recyclerViewdayHoursData.layoutManager=LinearLayoutManager(view.context).apply {
            this.orientation=LinearLayoutManager.HORIZONTAL
        }
        binding.recyclerViewdayHoursData.adapter=hoursAdapter
        binding.recyclerViewDaysInWeekData.layoutManager=LinearLayoutManager(view.context)
        binding.recyclerViewDaysInWeekData.adapter=daysAdapter
        if (UserStates.checkConnectionState(requireActivity())) {
            locationDataViewModel.liveData.observe(this) {
                when (it) {
                    is ApiState.Success -> {
                        val data = it.locationData
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
    override fun onResume() {
        super.onResume()
        locationDataViewModel.getAllFromNetwork()
    }

}