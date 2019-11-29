package com.sustaincsej.sustain_cedricsebevanjean.business

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

public class Co2Calculator(activity: AppCompatActivity) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private var haveLocation = false
    private val dawsonLongitude = -73.58854
    private val dawsonLatitude = 45.48775
    private val permissionFineLocation=android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permissionCoarseLocation=android.Manifest.permission.ACCESS_COARSE_LOCATION


    init{
        Log.d("Calculator", "initializing calc")


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        Log.d("Calculator", "fusedLocation init")
        var location: Location? = null
        fusedLocationClient.lastLocation
            .addOnSuccessListener { l : Location? -> location = l
            Log.i("Calculator", "entered listener")
                Log.d("Calculator", l.toString())
                if (location != null)
                {
                    this.haveLocation = true
                    this.currentLocation = location as Location
                }
            }


        Log.d("Calculator", "location = " +location.toString())

    }

    fun distanceToSchool() : Float
    {
        var results: FloatArray = floatArrayOf()
        if(this.haveLocation) {
            Log.d("Calculator", "in if")
            Location.distanceBetween(
                this.currentLocation.latitude,
                this.currentLocation.longitude,
                dawsonLatitude,
                dawsonLongitude,
                results
            )
            Log.d("Calculator", "distance calc")
            Log.d("Calculator", results[0].toString())
            return results[0]
        }
        return 0.0f
    }
}