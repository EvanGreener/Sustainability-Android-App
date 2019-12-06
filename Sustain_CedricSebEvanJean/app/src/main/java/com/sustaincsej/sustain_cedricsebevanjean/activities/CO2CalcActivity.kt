package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Context
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.X

import android.widget.*
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

/**
 * CO2CalcActivity this is the activity and API (internal class) that calculates and displays CO2 emissions
 * and Tree offset.
 * Extends AdapterView.OnItemSelectedListener to be able to react to the user selecting different
 * Values from the spinner
 *
 * @author Cedric Richards
 */
class CO2CalcActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {
    private lateinit var calculator: Co2Calculator
    private var transportMode = "Car Diesel"
    //if false destination is school
    private var destinationHome = "School"
    private var vehicleEfficiency = 123.4f


    /**
     * The calculator is called in oncreate, but this is not the only place it is called, this is only
     * its initial call
     * We set up the spinner and the toggle buttons functionality here.
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Calculator", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_co2_calc)
        Log.d("Calculator", "before calc")
        this.calculator = Co2Calculator()
        this.calculator.execute(transportMode, destinationHome)
        showSpinner()
        setUpToggleButton()



    }
    fun returnInfo(array: Array<Float>) : Array<Float>
    {
        return array
    }
    fun calculate(array: Array<String>) :  AsyncTask<String, Float, Float>
    {
        this.calculator = Co2Calculator()
        return this.calculator.execute(array[0], array[1])
    }
    /**
     * Everytime the button is pushed and the value of destinationHome is changed calculator is
     * reinitialized and executed, it cannot be executed without being reinitialized.
     *
     */
    private fun setUpToggleButton()
    {
        Log.d("Calculator", "setUpToggleButton")
        val toggle: ToggleButton = findViewById(R.id.co2calc_homeschool_toggle)
        toggle.setOnCheckedChangeListener { _, isHome ->
            if(isHome) {
                destinationHome = "Home"
            }
            else{
                destinationHome = "School"
            }
            Log.i("Calculator", destinationHome.toString())
            this.calculator = Co2Calculator()
            this.calculator.execute(transportMode, destinationHome)
        }

    }

    /**
     * Sets the spinner to have an onItem listener, and to listen to the methods of this class
     * as its listener.
     *
     */
    private fun showSpinner() {
        Log.d("Calculator", "ShowSpinner")
        val travelModeSpinner = findViewById(R.id.co2calc_travelmode_spinner) as Spinner
        val dataAdapter = ArrayAdapter<String>(
            this, R.layout.simple_spinner, resources.getStringArray(R.array.newtrip_travel_modes))
        travelModeSpinner.adapter = dataAdapter
        travelModeSpinner.onItemSelectedListener = this

    }

    /**
     * Mandatory for AdapterView.OnItemSelectedListener but unused by the algorythm of this activity.
     *
     */
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Everytime transport Mode is changed by the user the calculator is reinitialized and executed. It
     * cannot be executed without being reinitialized.
     *
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("Calculator", "OnitemSelected")
        transportMode = parent!!.getItemAtPosition(position) as String
        Log.i("Calculator", transportMode)
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

        this.calculator = Co2Calculator()
        this.calculator.execute(transportMode, destinationHome)
    }

    /**
     * The CO2Calculator API. The initial values all of its private fields will be overwritten
     * before being used unless something goes wrong, such as there is no location in the device calling the API
     * In this case these values will be used and the app will not crash.
     *
     *
     */
    private inner class Co2Calculator : AsyncTask<String, Float, Float>() {

        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private lateinit var currentLocation: Location
        private var haveLocation = false
        private var schoolLongitude = -73.58854
        private var schoolLatutude = 45.48775
        private var homeLongitude = 0.0
        private var homeLatutude = 0.0
        private lateinit var transportMode : String
        private lateinit var destinationHome : String
        private var currentLatitude = 0.0
        private var currentLongitude = 0.0

        /**
         * This Async task calls makes another asyncronous call, which is why onProgress update is used
         * To update the UI as this function is actually unable to return data at a usefull time,
         * but onProgressUpdate can, which is why publishProgress is called
         *
         */
        override fun doInBackground(vararg types : String): Float{
            Log.d("Calculator", "initializing calc")

            transportMode = types[0]
            destinationHome = types[1]

            curl -X GET -H "Accept: application/json" -H "Authorization: Bearer longtokenhere" "https://hostname/api/v1/tripinfo?fromlatitude=45.4908788&fromlongitude=-73. 588405&                          tolatitude=45.4908788&tolongitude=-73. 588405&mode=publicTransport"

            val queue = Volley.newRequestQueue(this)
            val url = "http://www.google.com"

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    // Display the first 500 characters of the response string.
                    textView.text = "Response is: ${response.substring(0, 500)}"
                },
                Response.ErrorListener { textView.text = "That didn't work!" })

            // Add the request to the RequestQueue.
            queue.add(stringRequest)




            Log.d("Calculator", "execute ended")
            return 0.0f
        }

        /**This method receives the distance to and from the destination as an argument, and receives
         * Local user selections and uses them to calculate and display the total CO2 and the treeOffset
         *
         * @see calculateTrees
         * @see calculateCO2G
         * @param result this is the distance in meters calculated by the api
         */
        override fun onProgressUpdate(vararg result: Float?){
            Log.d("Calculator", "onProgressUpdate")
            var cO2View = findViewById<TextView>(R.id.co2calc_totalco2)
            var totalCO2 = calculateCO2G(result[0] as Float)
            cO2View.text = totalCO2.toString()
            var treeOffSetView = findViewById<TextView>(R.id.co2calc_trees)
            var treeOffSet = calculateTrees(totalCO2)
            treeOffSetView.text = treeOffSet.toString()
            var distanceKM = result[0] as Float / 1000.0f
            var array = arrayOf(
                distanceKM,
                totalCO2,
                this.currentLocation.latitude.toFloat(),
                this.currentLocation.longitude.toFloat()
            )
            returnInfo(array)
        }

        /**
         * The calculation done to produce the total CO2 is done in grams and kilometers, the distance is adgusted
         * to accomodate for that.
         *
         * @param distance this is the distance from the last location recorded by the device and the destination
         */
        private fun calculateCO2G(distance: Float): Float{
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

        /**
         * ThisFormula assumes that any planted tree is harvested after a year and needs to be replaced
         * as a tree will absorb 59.7g CO2 per day, and the formula assumes that the user makes the trip everyday
         *
         */
        private fun calculateTrees(cO2InGrams: Float): Float
        {
            Log.d("Calculator", "calculateTrees")
            val gramsPerDayPerTree = 59.7f
            var treesToOffset = cO2InGrams / gramsPerDayPerTree
            return treesToOffset
        }

        /**
         * This function will return 0.0 as distance, turning all displayed information to 0.0
         * If there is no location in the devices memory.
         *
         * @return the distance to the destination in meters as a Float
         */
        fun distanceToDestination() : Float
        {
            Log.d("Calculator", "distanceToSchool")
            var results: FloatArray = floatArrayOf(1.0f)
            Log.d("Calculator", "location = " +currentLocation.toString())
            if(this.haveLocation) {
                Log.d("Calculator", "in if")
                if(destinationHome.equals("Home")) {

                    Location.distanceBetween(
                        this.currentLatitude,
                        this.currentLongitude,
                        homeLatutude,
                        homeLongitude,
                        results
                    )
                }
                else
                {
                    Location.distanceBetween(
                        this.currentLatitude,
                        this.currentLongitude,
                        schoolLatutude,
                        schoolLongitude,
                        results
                    )
                }
                Log.d("Calculator", "distance calc")
                Log.d("Calculator", results[0].toString())
                return results[0]
            }
            return 0.0f
        }

        /**
         * Retreives the information the user provided in the settings page when they first started the app
         * We use the location of the home and school to calculate distance relative to the devices location.
         *
         */
        override fun onPreExecute() {
            // set up the task here
            Log.d("Calculator", "onPreExecute")
            with(getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE).all){
                var schoolLat = this["SchoolLat"] as String
                var schoolLon = this["SchoolLon"] as String
                var homeLat = this["HomeLat"] as String
                var homeLon = this["HomeLon"] as String
                var curLat = this["CurrentLatitude"] as String
                var curLong = this["CurrentLongitude"] as String
                schoolLongitude = schoolLon.toDouble()
                schoolLatutude = schoolLat.toDouble()
                homeLongitude = homeLon.toDouble()
                homeLatutude = homeLat.toDouble()
                currentLatitude = curLat.toDouble()
                currentLongitude = curLong.toDouble()
            }
        }

    }
}
