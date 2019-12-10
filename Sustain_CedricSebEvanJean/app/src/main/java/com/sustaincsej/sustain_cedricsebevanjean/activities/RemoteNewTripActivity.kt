package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.httprequests.APICall

/**
 * Activity to add a trip to the remote php database
 *
 * @author Cedric Richards
 * @author Jean Robatto
 */
class RemoteNewTripActivity : AppCompatActivity(),  AdapterView.OnItemSelectedListener {

    private var travelMode = "Car Diesel"
    private var travelModeToAPI = "car"
    private var destination = "school"
    private var engine = "diesel"
    private var efficiency = "9.3"
    private var schoolLongitude = 0.0
    private var schoolLatitude = 0.0
    private var homeLongitude = 0.0
    private var homeLatitude = 0.0
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private var email : String = ""
    private var password : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        setContentView(R.layout.activity_remote_new_trip)

        showSpinner()
        retrieveCoords()
        setUpButtons()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("No need to implement. Useless")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "onItemSelected")
        travelMode = parent!!.getItemAtPosition(position) as String
        if(travelMode == "Car Diesel" || travelMode == "Carpool (3) Diesel")
        {engine = "diesel"}
        else if(travelMode == "Car Gas" || travelMode == "Carpool (3) Gas")
        {engine = "gasoline"}

        when (travelMode) {
            "Car Diesel" -> {efficiency = "9.3"
                travelModeToAPI = "car"}
            "Car Gas" -> {efficiency ="10.6"
                travelModeToAPI = "car"}
            "Carpool (3) Diesel" -> {efficiency ="3.1"
                travelModeToAPI = "carpool"}
            "Carpool (3) Gas" -> {efficiency ="3.53"
                travelModeToAPI = "carpool"}
            "Public Transit" -> {efficiency ="4.62"
                travelModeToAPI = "publicTransport"}
            "Walk" -> {efficiency ="0.0"
                travelModeToAPI = "pedestrian"}
            "Bike" -> {efficiency ="0.0"
                travelModeToAPI = "bicycle"}
        }
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
        Toast.makeText(this, resources.getString(R.string.newtrip_remote_home_selected), Toast.LENGTH_SHORT).show()
    }

    private fun handleSchoolClick(view: View) {
        destination = "school"
        Toast.makeText(this, resources.getString(R.string.newtrip_remote_school_selected), Toast.LENGTH_SHORT).show()
    }

    private fun handleSaveClick(view: View){
        Log.d(TAG, "saveTrip")
        val destLong: Double
        val destLat: Double
        var jsonString = ""
        if(destination == "home"){
            destLong = homeLongitude
            destLat = homeLatitude
        }
        else{
            destLong = schoolLongitude
            destLat = schoolLatitude
        }
        if(travelModeToAPI.equals("car") || travelModeToAPI.equals("carpool")) {
            jsonString =
                "{\"fromlatitude\":\"$currentLatitude\", \"fromlongitude\":\"$currentLongitude\", \"tolatitude\":\"$destLat\", \"tolongitude\":\"$destLong\",\"mode\":\"$travelModeToAPI\", \"engine\":\"$engine\"}"
        }
        else{
            jsonString = "{\"fromlatitude\":\"$currentLatitude\", \"fromlongitude\":\"$currentLongitude\",\"tolatitude\":\"$destLat\",  \"tolongitude\":\"$destLong\",\"mode\":\"$travelModeToAPI\", \"engine\":\"$engine\", \"consumption\"=\"$efficiency\"}"
            Log.d(TAG, jsonString)
        }
        val apiCall =  APICall("http://carbon-emission-tracker-team-7.herokuapp.com/api/v1/addtrip", "POST", email, password, jsonString)
        val result = apiCall.execute().get()
        Log.i(TAG, result.toString())
        startActivity(Intent(this, RemoteTripLogActivity::class.java))
        finish()
    }

    private fun retrieveCoords()
    {
        Log.d(TAG, "onPreExecute")
        with(getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE).all){
            val schoolLat = this["SchoolLat"] as String
            val schoolLon = this["SchoolLon"] as String
            val homeLat = this["HomeLat"] as String
            val homeLon = this["HomeLon"] as String
            val curLat = this["CurrentLatitude"] as String
            val curLong = this["CurrentLongitude"] as String
            email = this["Email"] as String
            password = this["Password"] as String
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
        val travelModeSpinner = findViewById<Spinner>(R.id.newtrip_travelmode_spinner)
        val dataAdapter = ArrayAdapter<String>(
            this, R.layout.simple_spinner, resources.getStringArray(R.array.newtrip_travel_modes))
        travelModeSpinner.adapter = dataAdapter
        travelModeSpinner.onItemSelectedListener = this

    }

    companion object {
        private const val TAG = "RemoteNewTripActivity"
    }
}