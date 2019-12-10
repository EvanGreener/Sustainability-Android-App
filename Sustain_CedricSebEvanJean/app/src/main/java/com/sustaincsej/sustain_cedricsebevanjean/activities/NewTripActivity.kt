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
import com.google.gson.Gson

import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.httprequests.APICall
import com.sustaincsej.sustain_cedricsebevanjean.models.CurrentWeather
import com.sustaincsej.sustain_cedricsebevanjean.models.TravelMode
import com.sustaincsej.sustain_cedricsebevanjean.models.TripInfoResponse
import org.json.JSONObject

/**
 * Activity that allows the user to create a new trip from their current location to the location of their
 * choosing.
 *
 * @author Evan Greenstein
 * @author Sebastien Palin
 */
class NewTripActivity : AppCompatActivity(),  AdapterView.OnItemSelectedListener {

    private lateinit var currentLocation: Location
    private var haveLocation = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var destinationLat: EditText
    private lateinit var destinationLon: EditText
    private lateinit var reason: EditText
    private lateinit var travelmode: Spinner

    private var currentLat = 0.0 //default value
    private var currentLon = 0.0 //default value
    private val destinationLatDefault = 45.48775
    private val destinationLonDefault = -73.58854
    private var distance = 0.0
    private var co2 = 0.0
    private var travelMode = "Car Diesel"

    private lateinit var co2Txt : TextView
    private lateinit var distanceTxt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip)

        showSpinner()

        destinationLat = findViewById(R.id.newtrip_tolat)
        destinationLon = findViewById(R.id.newtrip_tolon)
        travelmode = findViewById(R.id.newtrip_travelmode_spinner)
        reason = findViewById(R.id.newtrip_reason)

        co2Txt = findViewById(R.id.newtrip_totalco2)
        distanceTxt = findViewById(R.id.newtrip_distancekm)

        //Set button click events
        findViewById<Button>(R.id.newtrip_local_btn).setOnClickListener {
            onClick(it.id)
        }
        findViewById<Button>(R.id.newtrip_remote_btn).setOnClickListener {
            onClick(it.id)
        }

        //Set on change events
        destinationLat.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateValues(findViewById<Spinner>(R.id.newtrip_travelmode_spinner).selectedItem.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?){}
        })

        //Set on change events
        destinationLon.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateValues(findViewById<Spinner>(R.id.newtrip_travelmode_spinner).selectedItem.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}
        })

        //Set fields as needed
        val preset = intent.getStringExtra("preset")

        val prefs = getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE)

        if (preset == "home") {
            destinationLat.setText(prefs.getString("HomeLat", ""))
            destinationLon.setText(prefs.getString("HomeLon", ""))
            destinationLat.isEnabled = false
            destinationLon.isEnabled = false
        } else if (preset == "school") {
            destinationLat.setText(prefs.getString("SchoolLat", ""))
            destinationLon.setText(prefs.getString("SchoolLon", ""))
            destinationLat.isEnabled = false
            destinationLon.isEnabled = false
        }
    }

    /**
     * Sets the spinner to have an onItem listener, and to listen to the methods of this class
     * as its listener.
     *
     */
    private fun showSpinner() {
        Log.d("Calculator", "ShowSpinner")
        val travelModeSpinner = findViewById(R.id.newtrip_travelmode_spinner) as Spinner
        val dataAdapter = ArrayAdapter<String>(
            this, R.layout.simple_spinner, resources.getStringArray(R.array.newtrip_travel_modes))
        travelModeSpinner.adapter = dataAdapter
        travelModeSpinner.onItemSelectedListener = this

    }

    /**
     * Function that will create a new trip from the user's current location to where the coordinates they
     * inputted.
     *
     * @param id The id of the button clicked that called this function
     */
    private fun onClick(id: Int) {
        val lat =  destinationLat.text.toString()
        val lon = destinationLon.text.toString()
        var travelmode = travelmode.selectedItem.toString()
        val reason = findViewById<EditText>(R.id.newtrip_reason).text.toString()

        var latDouble = lat.toDouble()
        var lonDouble = lon.toDouble()
        val replyIntent = Intent()

        if (lat.isEmpty() || lon.isEmpty() || travelmode.isEmpty() || reason.isEmpty()){
            Toast.makeText(this, resources.getString(R.string.not_filled_out_not_saved), Toast.LENGTH_SHORT).show()
            return
        }
        else {
            latDouble = lat.toDouble()
            lonDouble = lon.toDouble()
        }

        when(travelmode){
            "Car Diesel" -> travelmode = TravelMode.CAR_DIESEL.name
            "Car Gas" -> travelmode = TravelMode.CAR_GAS.name
            "Carpool (3) Diesel" -> travelmode = TravelMode.CARPOOL_DIESEL.name
            "Carpool (3) Gas" -> travelmode = TravelMode.CARPOOL_GAS.name
            "Public Transit" -> travelmode = TravelMode.PUBLIC_TRANSIT.name
            "Walk" -> travelmode = TravelMode.WALK.name
            "Bike" -> travelmode = TravelMode.BIKE.name
        }

        when (id) {
            R.id.newtrip_remote_btn -> Log.i(TAG, "Remote button clicked")//FIRE REMOTE
            R.id.newtrip_local_btn ->{
                Log.i(TAG, "Local button clicked")

                val prefs = getSharedPreferences(getString(R.string.Preferences),Context.MODE_PRIVATE)
                val email = prefs.getString("Email", "")
                val password = prefs.getString("Password", "")

                getCurrentLocation()
                var apiCall = APICall("http://carbon-emission-tracker-team-7.herokuapp.com/api/v1/tripinfo", "GET", email!!, password!!, constructApiQueryString(latDouble, lonDouble))

                val response = Gson().fromJson(apiCall.execute().get().getJSONObject(0).toString(), TripInfoResponse::class.java)

                replyIntent.putExtra(FROM_LAT, currentLat)
                replyIntent.putExtra(FROM_LON, currentLon)
                replyIntent.putExtra(TO_LAT, latDouble)
                replyIntent.putExtra(TO_LON, lonDouble)
                replyIntent.putExtra(TRAVEL_MODE, travelmode)
                replyIntent.putExtra(REASON, reason)
                replyIntent.putExtra(DISTANCE, response.distance)
                replyIntent.putExtra(CO2, response.co2emissions)

                setResult(Activity.RESULT_OK, replyIntent)
                finish()

            }
        }
    }

    /**
     * Function that will get the current latitude and longitude of the user. Sets global variables
     * to the longitude and latitude instead of returning.
     */
    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@NewTripActivity)

        var location: Location? = null
        //Asyncronous call to get location the method will end before it completes its task.
        var locationRequest = LocationRequest.create()
        var locationCallback = LocationCallback()
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())

        fusedLocationClient.lastLocation
            .addOnSuccessListener { l : Location? -> location = l
                if (location != null) {
                    haveLocation = true
                    currentLocation = location as Location

                    currentLat = currentLocation.latitude
                    currentLon = currentLocation.longitude

                }
            }
    }

    /**
     * Function that will create an api query string for use of calling the remote api to get the co2 consumed
     * and the distanced traveled when creating a new trip.
     *
     * @param latDouble The double latitude of the destination
     * @param lonDouble The double longitude of the destinatino
     * @return A query String for the remote api
     */
    private fun constructApiQueryString(latDouble: Double, lonDouble: Double) : String {
        var basicJsonString =
            "{\"fromlatitude\":\"$currentLat\", \"fromlongitude\":\"$currentLon\",\"tolatitude\":\"$latDouble\",  \"tolongitude\":\"$lonDouble\","

        when {
            travelMode.equals("Car Diesel") -> basicJsonString += "\"mode\":\"car\", \"engine\":\"diesel\", \"consumption\":\"121.5\"}"
            travelMode.equals("Car Gas") -> basicJsonString += "\"mode\":\"car\", \"engine\":\"gasoline\", \"consumption\":\"123.4\"}"
            travelMode.equals("Carpool (3) Diesel") -> basicJsonString += "\"mode\":\"carpool\", \"engine\":\"diesel\", \"consumption\":\"121.5\"}"
            travelMode.equals("Carpool (3) Gas") -> basicJsonString += "\"mode\":\"carpool\", \"engine\":\"gasoline\", \"consumption\":\"123.4\"}"
            travelMode.equals("Public Transit") -> basicJsonString += "\"mode\":\"publicTransport\"}"
            travelMode.equals("Walk") -> basicJsonString += "\"mode\":\"pedestrian\"}"
            travelMode.equals("Bike") -> basicJsonString += "\"mode\":\"bicycle\"}"
        }

        return basicJsonString
    }

    /**
     * This is implemented so as to keep inheritance happy, but does nothing as it is not really needed
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Don't do anything with
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "onItemSelected")
        travelMode = parent!!.getItemAtPosition(position) as String
        updateValues(travelMode)
    }

    private fun calculateAndUpdate(travelMode: String, destLat: Double, destLon: Double) : DoubleArray {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@NewTripActivity)

        var location: Location? = null
        //Asyncronous call to get location the method will end before it completes its task.
        var locationRequest = LocationRequest.create()
        var locationCallback = LocationCallback()
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())

        fusedLocationClient.lastLocation
            .addOnSuccessListener { l : Location? -> location = l
            if (location != null) {
                haveLocation = true
                currentLocation = location as Location
                this.distance = distanceToDestination(destLat, destLon)
                distanceTxt.text = this.distance.toString()

                this.co2 = calculateCO2G(distance, travelMode)
                co2Txt.text = this.co2.toString()


                currentLat = currentLocation.latitude
                currentLon = currentLocation.longitude

            }
        }

        return doubleArrayOf(distance, co2)
    }

    /**
     * Updates the values for co2 emissions and distance
     */
    private fun updateValues(travelMode: String) {
        val info : DoubleArray
        if (destinationLat.text.isEmpty() || destinationLon.text.isEmpty()){
            info = calculateAndUpdate(travelMode, destinationLatDefault, destinationLonDefault)
        }
        else{
            info = calculateAndUpdate(travelMode, destinationLat.text.toString().toDouble(), destinationLon.text.toString().toDouble())
        }

        distance = info[0]
        co2 = info[1]
    }

    /**
     * This function will return 0.0 as distance, turning all displayed information to 0.0
     * If there is no location in the devices memory.
     *
     * @return the distance to the destination in meters as a Float
     */
    private fun distanceToDestination(destLat: Double, destLon: Double): Double
    {
        Log.d("Calculator", "distanceToSchool")
        var results: FloatArray = floatArrayOf(1.0f)
        Log.d("Calculator", "location = " +currentLocation.toString())
        if(this.haveLocation) {

            Location.distanceBetween(
                this.currentLocation.latitude,
                this.currentLocation.longitude,
                destLat,
                destLon,
                results
            )

            return results[0].toDouble()
        }
        return 0.0
    }

    /**
     * The calculation done to produce the total CO2 is done in grams and kilometers, the distance is adgusted
     * to accomodate for that.
     *
     * @param distance this is the distance from the last location recorded by the device and the destination
     */
    private fun calculateCO2G(distance: Double, transportMode : String ): Double{
        Log.d("Calculator", "calculateCO2G")
        var distanceKM = distance / 1000.0f
        var vehicleEfficiency = 0.0f
        if(transportMode.equals("Car Diesel"))
        {vehicleEfficiency = 121.5f}
        else if(transportMode.equals("Car Gas"))
        {vehicleEfficiency = 123.4f}
        else if(transportMode.equals("Carpool (3) Diesel"))
        {vehicleEfficiency = 40.5f}
        else if(transportMode.equals("Carpool (3) Gas"))
        {vehicleEfficiency = 41.1f}
        else if(transportMode.equals("Public Transit"))
        {vehicleEfficiency = 46.2f}
        else if(transportMode.equals("Walk"))
        {vehicleEfficiency = 0.0f}
        else if(transportMode.equals("Bike"))
        {vehicleEfficiency = 0.0f}
        var totalCO2 = distanceKM * vehicleEfficiency
        return totalCO2
    }

    companion object {
        private val TAG = "NewTripActivity"
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