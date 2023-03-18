package com.app.our.cskies.LocationGetter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.app.our.cskies.ActivityHome
import com.app.our.cskies.R
import com.app.our.cskies.messages.Toasts
import com.app.our.cskies.model.Setting
import com.app.our.cskies.model.UserCurrentLocation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.util.*


class FragmentLocationDetector() : DialogFragment(),GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, LocationListener  {

    lateinit var btnDoneMap:ImageButton

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mGoogleApiClient: GoogleApiClient
    lateinit var mGoogleMap: GoogleMap
    var pinSelected:Boolean=false
    private lateinit var mMapView: MapView
    var mode:Int=if(Setting.location==Setting.Location.GPS)0 else 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location_detector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(mode==1) {
            btnDoneMap=view.findViewById(R.id.imageButtonMapConfirm)
            btnDoneMap.setOnClickListener{
                if(pinSelected)
                {
                    val intent=Intent(requireActivity(),ActivityHome::class.java)
                    startActivity(intent)
                }else{
                    Toasts.SnakeToast(it,"Please Select Location First")
                }
            }
            mMapView = view.findViewById(R.id.mapView)
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
            view.visibility=View.GONE
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(request : LocationRequest, callback: LocationCallback) : Task<Void> {
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this.requireActivity())
        return fusedLocationProviderClient.requestLocationUpdates(
            request,
            callback, Looper.myLooper()
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
            requestPermissions()
        }
    }
    private val PermissionID: Int=10
    private  val myCallback=object :LocationCallback(){
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)

            val latitude=result.lastLocation?.latitude
            val longtude=result.lastLocation?.longitude
            UserCurrentLocation.latitude=latitude.toString()
            UserCurrentLocation.longitude=longtude.toString()
            Log.e("",UserCurrentLocation.latitude?:"00000000000000000000")
            fusedLocationProviderClient.removeLocationUpdates(this)
            val intent=Intent(requireActivity(),ActivityHome::class.java)
            startActivity(intent)
            dismiss()

        }
    }

    private fun requestNewLoaction(){
        val request=LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        requestLocationUpdates(request,myCallback)
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
                getLastLocation()
            }
        }
    }
    private fun setUpMap() {
         mMapView.getMapAsync { googleMap -> mGoogleMap = googleMap

             mGoogleMap.setOnMapLongClickListener{
                 pinSelected=true
                 mGoogleMap.clear()
                 mGoogleMap.addMarker(MarkerOptions().position(it))
                 UserCurrentLocation.longitude=it.latitude.toString()
                 UserCurrentLocation.longitude=it.longitude.toString()
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