package com.example.smartattendancesystem.services

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.smartattendancesystem.util.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber


typealias PolyLine = MutableList<LatLng>
typealias Polylines = MutableList<PolyLine>

class TrackingService : LifecycleService() {

     private lateinit var fusedLocationProviderClient : FusedLocationProviderClient

    companion object{
        val isTracking = MutableLiveData<Boolean>()
        val tracking : LiveData<Boolean> = isTracking
        val pathPoints = MutableLiveData<Polylines>()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                Constants.ACTION_START_OR_RESUME_SERVICE -> {
                    addEmptyPolyline()
                    isTracking.postValue(true)
                }
                Constants.ACTION_PAUSE_SERVICE ->{

                }
                Constants.ACTION_STOP_SERVICE ->{
                    stop()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }



    override fun onCreate() {
        super.onCreate()
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })

    }


    val locationCallback = object : LocationCallback(){
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if(isTracking.value!!){
                result?.locations?.let {locations ->
                    for(location in locations){
                        addPathPoint(location)
                    }
                }
            }
        }
    }



    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }


    private fun stop(){
        isTracking.postValue(false)
    }


    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking : Boolean){
        if(isTracking){
            val request = LocationRequest.create().apply {
                interval = Constants.LOCATION_UPDATE_INTERVAL
                fastestInterval = Constants.FASTEST_LOCATION_INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        }else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }


    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))
}