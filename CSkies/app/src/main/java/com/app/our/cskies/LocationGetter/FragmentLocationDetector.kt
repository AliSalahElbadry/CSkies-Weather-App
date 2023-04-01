package com.app.our.cskies.LocationGetter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.app.our.cskies.R
import com.app.our.cskies.alerts.view.FragmentAlertsPage
import com.app.our.cskies.favorites.view.FragmentFavoritesPage
import com.app.our.cskies.settings.view.FragmentSettingPage
import com.app.our.cskies.shard_pref.SharedPrefOps
import com.app.our.cskies.splash.SplashCall
import com.app.our.cskies.utils.Dialogs
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserCurrentLocation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.util.*


class FragmentLocationDetector() : DialogFragment(),GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, LocationListener  {

    lateinit var btnDoneMap:ImageButton
    lateinit var imageButtonMyLocation:ImageButton
    lateinit var searchView: SearchView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mGoogleApiClient: GoogleApiClient
    lateinit var mGoogleMap: GoogleMap
    var pinSelected:Boolean=false
    private lateinit var mMapView: MapView
    var mode:Int=if(Setting.location== Setting.Location.GPS)0 else 1
    var isFavorite:Boolean=false
    var isSetting:Boolean=false
    var isAlert:Boolean=false
    var myLocation=false
    lateinit var loadingAnim:LottieAnimationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location_detector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnDoneMap=view.findViewById(R.id.imageButtonMapConfirm)
        searchView=view.findViewById(R.id.searchViewPlace)
        mMapView = view.findViewById(R.id.mapView)
        loadingAnim=view.findViewById(R.id.loadingAnim)
        imageButtonMyLocation=view.findViewById(R.id.imageButtonMyLocation)
        if(mode==1) {
            loadingAnim.visibility=View.INVISIBLE
            imageButtonMyLocation.setOnClickListener{
                Toast.makeText(requireContext(),if(Setting.getLang()=="en") "Loading Your Location" else "جاري تحديد موقعك",Toast.LENGTH_SHORT).show()
                imageButtonMyLocation.visibility=View.INVISIBLE
                loadingAnim.visibility=View.VISIBLE
                loadingAnim.playAnimation()
                myLocation=true
                getLastLocation()
            }
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                override fun onQueryTextSubmit(query: String?): Boolean {
                  val latlng=query?.let { UserCurrentLocation.getLatLonForAddress(it,requireContext().applicationContext) }
                   if(latlng!=null) {
                       val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 10F)
                       if(!isFavorite&&!isAlert) {
                           UserCurrentLocation.latitude = latlng.latitude.toString()
                           UserCurrentLocation.longitude = latlng.longitude.toString()
                       }else
                       {
                           UserCurrentLocation.favoriteLat = latlng.latitude.toString()
                           UserCurrentLocation.favoriteLon = latlng.longitude.toString()
                       }
                       pinSelected=true
                       mGoogleMap.clear()
                       mGoogleMap.addMarker(MarkerOptions().position(latlng))
                       mGoogleMap.animateCamera(cameraUpdate)

                   }
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return  false
                }

            })
            btnDoneMap.setOnClickListener{
                if(pinSelected)
                {

                    if(!isFavorite&&!isSetting&&!isAlert) {
                        val pref = SharedPrefOps(requireContext().applicationContext)
                        pref.insertInData()
                        pref.saveLastLocation()
                        (requireActivity() as SplashCall).showHome()
                        mMapView.onDestroy()
                        dismiss()
                    }else if(isFavorite){
                        val fragmentShowFavoritesPage = FragmentFavoritesPage()
                        fragmentShowFavoritesPage.isNew=true
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.my_host_fragment, fragmentShowFavoritesPage, null)
                            .addToBackStack(null)
                            .commit()
                    }else if(isSetting)
                    {
                        val ftragmentSettings = FragmentSettingPage()
                        ftragmentSettings.isLocationMapSet=true
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.my_host_fragment, ftragmentSettings, null)
                            .addToBackStack(null)
                            .commit()
                    }else if(isAlert){
                        val ftragmentAlerts = FragmentAlertsPage()
                        ftragmentAlerts.isLocationMapSet=true
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.my_host_fragment, ftragmentAlerts, null)
                            .addToBackStack(null)
                            .commit()
                    }
                }else{
                    Dialogs.SnakeToast(it,"Please Select Location First")
                }
            }
            mMapView.onCreate(savedInstanceState)
            mMapView.onResume()
            mGoogleApiClient = GoogleApiClient.Builder(requireContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
            setUpMap()
        }else if(mode==0)
        {
            btnDoneMap.visibility=View.GONE
            mMapView.visibility=View.GONE
            searchView.visibility=View.GONE
            imageButtonMyLocation.visibility=View.GONE
            loadingAnim.playAnimation()
            getLastLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        if(mode==0) {
            loadingAnim.playAnimation()
            getLastLocation()

        }
        if(!isAlert&&!isSetting&&!isFavorite)
        {
            if(mode==1) {
                val window = dialog!!.window
                val size = Point()
                val display = window!!.windowManager.defaultDisplay
                display.getSize(size)
                window.setLayout((size.x * 0.959).toInt(), (size.y * 0.9).toInt())
                window.setGravity(Gravity.CENTER)
            }else{
                val window=dialog!!.window
                window?.setBackgroundDrawableResource(R.drawable.back_transparent)
                window?.setGravity(Gravity.CENTER)
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(
        request: LocationRequest,
        callback: LocationCallback,
        myLooper: Looper
    ) : Task<Void> {
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this.requireActivity())
        return fusedLocationProviderClient.requestLocationUpdates(
            request,
            callback, myLooper
        )
    }

    private fun getLastLocation():Unit{
        if(checkPermissions())
        {
            if(isLocationEnabled())
            {
                requestNewLoaction()
            }else{
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            loadingAnim.pauseAnimation()
            requestPermissions()
        }
    }
    private val PermissionID: Int=10
    private  val myCallback=object :LocationCallback(){
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            try {
                val latitude = result.lastLocation?.latitude
                val longtude = result.lastLocation?.longitude
                UserCurrentLocation.latitude = latitude.toString()
                UserCurrentLocation.longitude = longtude.toString()
                val pref = SharedPrefOps(requireActivity().applicationContext)
                pref.insertInData()
                pref.saveLastLocation()
                fusedLocationProviderClient.removeLocationUpdates(this)
                loadingAnim.cancelAnimation()
                loadingAnim.visibility = View.GONE
                if (!isSetting&&!myLocation&&!isFavorite&&!isAlert) {
                    (requireActivity() as SplashCall).showHome()
                } else {
                    if (myLocation) {
                        myLocation=false
                        imageButtonMyLocation.visibility = View.VISIBLE
                        val cameraUpdate =
                            CameraUpdateFactory.newLatLngZoom(LatLng(latitude!!, longtude!!), 10F)
                        if (!isFavorite && !isAlert) {
                            UserCurrentLocation.latitude = latitude.toString()
                            UserCurrentLocation.longitude = longtude.toString()
                        } else {
                            UserCurrentLocation.favoriteLat = latitude.toString()
                            UserCurrentLocation.favoriteLon = longtude.toString()
                        }
                        pinSelected = true
                        mGoogleMap.clear()
                        mGoogleMap.addMarker(MarkerOptions().position(LatLng(latitude, longtude)))
                        mGoogleMap.animateCamera(cameraUpdate)
                    } else {
                        val ftragmentSettings = FragmentSettingPage()
                        ftragmentSettings.isLocationMapSet = true
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.my_host_fragment, ftragmentSettings, null)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }catch (e:java.lang.IllegalStateException)
            {
                Log.e("error",e.toString())
            }
        }
    }

    private fun requestNewLoaction(){
        val request=LocationRequest()
        request.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        request.interval=0
        requestLocationUpdates(request,myCallback, Looper.myLooper()!!)
    }

    private fun checkPermissions(): Boolean{
        var result = false
        if ((ContextCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)   &&(ContextCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)) {
            result  = true
        }
        return result
    }
    private fun isLocationEnabled():Boolean{
        val locationManager: LocationManager =this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
    private fun requestPermissions():Unit{
        ActivityCompat.requestPermissions(this.requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),PermissionID
        )
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== PermissionID)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                loadingAnim.playAnimation()
                getLastLocation()
            }
        }
    }
    private fun setUpMap() {
         mMapView.getMapAsync { googleMap -> mGoogleMap = googleMap

             mGoogleMap.setOnMapLongClickListener{
                 val latitude=it?.latitude
                 val longitude=it?.longitude
                 if(!isFavorite&&!isAlert) {
                     UserCurrentLocation.latitude = latitude.toString()
                     UserCurrentLocation.longitude = longitude.toString()
                 }else
                 {
                     UserCurrentLocation.favoriteLat = latitude.toString()
                     UserCurrentLocation.favoriteLon = longitude.toString()
                 }
                 pinSelected=true
                 mGoogleMap.clear()
                 mGoogleMap.addMarker(MarkerOptions().position(it))
             }
         }
    }
    override fun onConnected(p0: Bundle?) {
        if(checkPermissions())
             getLastLocation()
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e("","Suspended Connection")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("","Failed Connection")
    }

    override fun onLocationChanged(p0: Location) {
        Log.e("","Location Changed")
    }
}