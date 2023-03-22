package com.app.our.cskies.favorites.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.app.our.cskies.LocationGetter.FragmentLocationDetector
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.databinding.FragmentFavoritesPageBinding
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.favorites.viewmodel.FavoritesViewModel
import com.app.our.cskies.utils.Dialogs
import com.app.our.cskies.utils.UserStates
import com.app.our.cskies.weather_data_show.view.FragmentShowLocationData
import kotlinx.coroutines.launch


class FragmentFavoritesPage : Fragment(),IOnClickItemListener {

    private val favoritesAdapter: FavoritesAdapter= FavoritesAdapter(mutableListOf(),this)
    private lateinit var favoritesViewModel: FavoritesViewModel
    lateinit var binding: FragmentFavoritesPageBinding
    var isNew:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding= FragmentFavoritesPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel= FavoritesViewModel(Repository.getInstance(binding.root.context))
        binding.recyclerView.layoutManager=LinearLayoutManager(this.context)
        binding.recyclerView.adapter=favoritesAdapter
        binding.fab.setOnClickListener{
            if(UserStates.checkConnectionState(requireActivity())) {
                var fragmentShowLocationDetector = FragmentLocationDetector()
                fragmentShowLocationDetector.mode = 1
                fragmentShowLocationDetector.isFavorite = true
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(
                        com.app.our.cskies.R.id.my_host_fragment,
                        fragmentShowLocationDetector,
                        null
                    )
                    .addToBackStack(null)
                    .commit()
            }else{
                Dialogs.SnakeToast(binding.root,"Please Check Your Connection")
            }
        }
           if(isNew) {
               favoritesViewModel.setUpFavoriteLocation(this.context!!)
           }
           favoritesViewModel.getAllFavoriteLocations()
        lifecycleScope.launch {
           favoritesViewModel.liveData.observe(this@FragmentFavoritesPage, Observer {
               favoritesAdapter.locations= it as MutableList<Location>
               favoritesAdapter.notifyDataSetChanged()
           })
       }
        lifecycleScope.launch{
            favoritesViewModel.locationData.observe(this@FragmentFavoritesPage, Observer {
                 var fragmentShowLocationData = FragmentShowLocationData()
                 fragmentShowLocationData.data=it
                 fragmentShowLocationData.mode=2
                Log.e("","Data Arrived")
                 activity!!.supportFragmentManager.beginTransaction()
                    .replace(com.app.our.cskies.R.id.my_host_fragment, fragmentShowLocationData, null)
                    .addToBackStack(null)
                    .commit()
            })
        }
        lifecycleScope.launch{
            favoritesViewModel.newLocation.observe(this@FragmentFavoritesPage, Observer {
                favoritesAdapter.locations.add(it)
                favoritesAdapter.notifyDataSetChanged()
                Dialogs.SnakeToast(binding.root,"New Favorite Added Successfully")
            })
        }
    }

    override fun onClick(location: Location) {
        favoritesViewModel.getLocationData(location)
    }

    override fun onClickDelete(location: Location) {
        favoritesViewModel.deleteLocation(location)
        Dialogs.SnakeToast(binding.root,"Done Delete Location")
    }
}