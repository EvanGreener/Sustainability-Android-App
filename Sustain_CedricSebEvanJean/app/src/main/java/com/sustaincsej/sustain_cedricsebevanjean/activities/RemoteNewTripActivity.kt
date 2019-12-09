package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.httprequests.APICall
import com.sustaincsej.sustain_cedricsebevanjean.models.TravelMode

class RemoteNewTripActivity : AppCompatActivity(),  AdapterView.OnItemSelectedListener {

    var travelMode = "Car Diesel"
    var travelModeToAPI = "car"
    var destination = "school"
    var engine = "diesel"
    var efficiency = "9.3"
    var schoolLongitude = 0.0
    var schoolLatitude = 0.0
    var homeLongitude = 0.0
    var homeLatitude = 0.0
    var currentLatitude = 0.0
    var currentLongitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        setContentView(R.layout.activity_remote_new_trip)

        showSpinner()
        Log.i(TAG, "afterSpinner")
        retrieveCoords()
        Log.i(TAG, "afterreTreive")
        setUpButtons()
        Log.i(TAG, "afterButtons")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("No need to implement. Useless")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "onItemSelected")
        travelMode = parent!!.getItemAtPosition(position) as String
        if(travelMode.equals("Car Diesel") || travelMode.equals("Carpool (3) Diesel"))
        {engine = "diesel"}
        else if(travelMode.equals("Car Gas") || travelMode.equals("Carpool (3) Gas"))
        {engine = "gas"}

        when(travelMode)
        {
            "Car Diesel" -> {efficiency = "9.3"; travelModeToAPI = "car"}
            "Car Gas" -> {efficiency ="10.6"; travelModeToAPI = "car"}
            "Carpool (3) Diesel" -> {efficiency ="3.1"; travelModeToAPI = "carpool"}
            "Carpool (3) Gas" -> {efficiency ="3.53"; travelModeToAPI = "carpool"}
            "Public Transit" -> {efficiency ="4.62"; travelModeToAPI = "publicTransport"}
            "Walk" -> {efficiency ="0"; travelModeToAPI = "pedestrian"}
            "Bike" -> {efficiency ="0"; travelModeToAPI = "bicycle"}
        }
        //updateValues(travelMode)
    }

    private fun setUpButtons(){
        findViewById<Button>(R.id.remotenewtrip_home).setOnClickListener {
            handleHomeClick(it)
        }
        findViewById<Button>(R.id.remotenewtrip_school).setOnClickListener {
            handleSchoolClick(it)
        }
        findViewById<Button>(R.id.remote_trip_save_btn).setOnClickListener {
            handleSaveClick(it)
        }
    }
    private fun handleHomeClick(view: View) {
        destination = "home"
    }

    private fun handleSchoolClick(view: View) {
        destination = "school"
    }

    private fun handleSaveClick(view: View){
        Log.d(TAG, "saveTrip")
        var destLong = 0.0
        var destLat = 0.0
        var jsonString = ""
        if(destination.equals("home")){
            destLong = homeLongitude
            destLat = homeLatitude
        }
        else{
            destLong = schoolLongitude
            destLat = schoolLatitude
        }
        if(travelModeToAPI.equals("car") || travelModeToAPI.equals("carpool")) {
            jsonString =
                "{\"fromlatitude\":\"$currentLatitude\", \"fromlongitude\":\"$currentLongitude\",\"tolatitude\":\"$destLat\",  \"tolongitude\":\"$destLong\",\"mode\":\"$travelModeToAPI\", \"engine\":\"$engine\"}"
        }
        else{
            jsonString = "{\"fromlatitude\":\"$currentLatitude\", \"fromlongitude\":\"$currentLongitude\",\"tolatitude\":\"$destLat\",  \"tolongitude\":\"$destLong\",\"mode\":\"$travelModeToAPI\"}"
        }
        var apiCall =  APICall("http://carbon-emission-tracker-team-7.herokuapp.com/api/v1/addtrip", "POST", "robatto.jeanmarie@gmail.com", "password", jsonString)
        var result = apiCall.execute().get()
        Log.i(TAG, result.toString())
        finish()
    }

    private fun retrieveCoords()
    {
        Log.d(TAG, "onPreExecute")
        with(getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE).all){
            var schoolLat = this["SchoolLat"] as String
            var schoolLon = this["SchoolLon"] as String
            var homeLat = this["HomeLat"] as String
            var homeLon = this["HomeLon"] as String
            var curLat = this["CurrentLatitude"] as String
            var curLong = this["CurrentLongitude"] as String
            schoolLongitude = schoolLon.toDouble()
            schoolLatitude = schoolLat.toDouble()
            homeLongitude = homeLon.toDouble()
            homeLatitude = homeLat.toDouble()
            currentLatitude = curLat.toDouble()
            currentLongitude = curLong.toDouble()
        }
    }

    private fun showSpinner() {
        Log.d("Calculator", "ShowSpinner")
        val travelModeSpinner = findViewById(R.id.newtrip_travelmode_spinner) as Spinner
        val dataAdapter = ArrayAdapter<String>(
            this, R.layout.simple_spinner, resources.getStringArray(R.array.newtrip_travel_modes))
        travelModeSpinner.adapter = dataAdapter
        travelModeSpinner.onItemSelectedListener = this

    }

    companion object {
        private val TAG = "RemoteNewTripActivity"
        const val FROM_LAT = "from-lat"
        const val FROM_LON  = "from-lon"
        const val TO_LAT = "to-lat"
        const val TO_LON = "to-lon"
        const val TRAVEL_MODE = "travelmode"
        const val REASON = "lat"
        const val DISTANCE = "distance"
        const val CO2 = "co2"

    }
}