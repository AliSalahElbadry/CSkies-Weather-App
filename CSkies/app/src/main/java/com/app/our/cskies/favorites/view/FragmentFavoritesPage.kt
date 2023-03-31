package com.app.our.cskies.favorites.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.our.cskies.LocationGetter.FragmentLocationDetector
import com.app.our.cskies.R
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.databinding.FragmentFavoritesPageBinding
import com.app.our.cskies.dp.LocalDataSource
import com.app.our.cskies.dp.LocalSourceImpl
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.favorites.viewmodel.FavoritesViewModel
import com.app.our.cskies.favorites.viewmodel.FavoritesViewModelFactory
import com.app.our.cskies.network.RemoteSourceImpl
import com.app.our.cskies.utils.Dialogs
import com.app.our.cskies.utils.Setting
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

        val factory= FavoritesViewModelFactory(Repository.getInstance(LocalSourceImpl.getInstance(requireActivity().applicationContext),
            RemoteSourceImpl.getInstance()!!))
        favoritesViewModel= ViewModelProvider(this,factory)[FavoritesViewModel::class.java]
        requireActivity().title = resources.getString(R.string.favorites)
        binding.recyclerView.layoutManager=LinearLayoutManager(this.context)
        binding.recyclerView.adapter=favoritesAdapter
        binding.fab.setOnClickListener{
            if(UserStates.checkConnectionState(requireActivity())) {
                val fragmentShowLocationDetector = FragmentLocationDetector()
                fragmentShowLocationDetector.mode = 1
                fragmentShowLocationDetector.isFavorite = true
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        com.app.our.cskies.R.id.my_host_fragment,
                        fragmentShowLocationDetector,
                        null
                    )
                    .addToBackStack(null)
                    .commit()
            }else{
                Dialogs.SnakeToast(binding.root,if(Setting.getLang()=="en")"Please Check Your Connection" else "تأكد من اتصال الانترنت")
            }
        }
           if(isNew) {
               favoritesViewModel.setUpFavoriteLocation(requireContext())
           }
           favoritesViewModel.getAllFavoriteLocations()
        lifecycleScope.launch {
           favoritesViewModel.liveData.observe(viewLifecycleOwner, Observer {
               if(it.isNotEmpty()){
                   binding.imageView.visibility=View.INVISIBLE
               }else{
                   binding.imageView.visibility=View.VISIBLE
               }
               favoritesAdapter.locations= it as MutableList<Location>
               favoritesAdapter.notifyDataSetChanged()
           })
       }
        lifecycleScope.launch{
            favoritesViewModel.locationData.observe(viewLifecycleOwner, Observer {
                 val fragmentShowLocationData = FragmentShowLocationData()
                 fragmentShowLocationData.data=it
                 fragmentShowLocationData.mode=2
                 requireActivity().supportFragmentManager.beginTransaction()
                    .replace(com.app.our.cskies.R.id.my_host_fragment, fragmentShowLocationData, null)
                     .addToBackStack(null)
                    .commit()

            })
        }
        lifecycleScope.launch{
            favoritesViewModel.newLocation.observe(viewLifecycleOwner, Observer {
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