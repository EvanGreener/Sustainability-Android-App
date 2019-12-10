package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.httprequests.APICall


/**
 * CO2CalcActivity this is the activity and API (internal class) that calculates and displays CO2 emissions
 * and Tree offset.
 * Extends AdapterView.OnItemSelectedListener to be able to react to the user selecting different
 * Values from the spinner
 *
 * @author Cedric Richards
 * @author Jean Robatto
 */
class CO2CalcActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {
    private lateinit var calculator: Co2Calculator
    private var transportMode = "Car Diesel"
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
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_co2_calc)
        Log.d(TAG, "before calc")
        this.calculator = Co2Calculator()
        this.calculator.execute(transportMode, destinationHome)
        showSpinner()
        setUpToggleButton()



    }

    /**
     * Everytime the button is pushed and the value of destinationHome is changed calculator is
     * reinitialized and executed, it cannot be executed without being reinitialized.
     *
     */
    private fun setUpToggleButton()
    {
        Log.d(TAG, "setUpToggleButton")
        val toggle: ToggleButton = findViewById(R.id.co2calc_homeschool_toggle)
        toggle.setOnCheckedChangeListener { _, isHome ->
            destinationHome = if(isHome) {
                "Home"
            } else{
                "School"
            }
            Log.i(TAG, destinationHome)
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
        Log.d(TAG, "ShowSpinner")
        val travelModeSpinner = findViewById<Spinner>(R.id.co2calc_travelmode_spinner)
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
        Log.d(TAG, "OnitemSelected")
        transportMode = parent!!.getItemAtPosition(position) as String
        Log.i(TAG, transportMode)
        if(transportMode == "Car Diesel")
        {vehicleEfficiency = 121.5f}
        else if(transportMode == "Car Gas")
        {vehicleEfficiency = 123.4f}
        else if(transportMode == "Carpool (3) Diesel")
        {vehicleEfficiency = 40.5f}
        else if(transportMode == "Carpool (3) Gas")
        {vehicleEfficiency = 41.1f}
        else if(transportMode == "Public Transit")
        {vehicleEfficiency = 46.2f}
        else if(transportMode == "Walk")
        {vehicleEfficiency = 0.0f}
        else if(transportMode == "Bike")
        {vehicleEfficiency = 0.0f}

        this.calculator = Co2Calculator()
        this.calculator.execute(transportMode, destinationHome)
    }

    companion object {
        private const val TAG = "CO2CalcActivity"
    }

    /**
     * The CO2Calculator API. The initial values all of its private fields will be overwritten
     * before being used unless something goes wrong, such as there is no location in the device calling the API
     * In this case these values will be used and the app will not crash.
     *
     *
     */
    @SuppressLint("StaticFieldLeak")
    private inner class Co2Calculator : AsyncTask<String, String, Float>() {

        private var schoolLongitude = -73.58854
        private var schoolLatitude = 45.48775
        private var homeLongitude = 0.0
        private var homeLatitude = 0.0
        private lateinit var transportMode : String
        private lateinit var destinationHome : String
        private var currentLatitude = 0.0
        private var currentLongitude = 0.0
        lateinit var apiCall : APICall
        private var email: String = ""
        private var password: String = ""


        /**
         * This Async task calls makes another asyncronous call, which is why onProgress update is used
         * To update the UI as this function is actually unable to return data at a usefull time,
         * but onProgressUpdate can, which is why publishProgress is called
         *
         */
        override fun doInBackground(vararg types : String): Float{
            Log.d(TAG, "initializing calc")

            transportMode = types[0]
            destinationHome = types[1]
            var travelModeToAPI = ""
            var efficiency = "0.0"
            var engine = "diesel"


            if(transportMode == "Car Diesel" || transportMode == "Carpool (3) Diesel")
            {engine = "diesel"}
            else if(transportMode == "Car Gas" || transportMode == "Carpool (3) Gas")
            {engine = "gasoline"}

            when (transportMode) {
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
            var destLong = 0.0
            var destLat = 0.0
            if(destinationHome == "Home"){
                destLong = homeLongitude
                destLat = homeLatitude
            }
            else{
                destLong = schoolLongitude
                destLat = schoolLatitude
            }

            val jsonString = "{\"fromlatitude\":\"$currentLatitude\", \"fromlongitude\":\"$currentLongitude\",\"tolatitude\":\"$destLat\",  \"tolongitude\":\"$destLong\",\"mode\":\"$travelModeToAPI\", \"engine\":\"$engine\", \"consumption\"=\"$efficiency\"}"

            publishProgress(jsonString)


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
        override fun onProgressUpdate(vararg result: String?){
            Log.d(TAG, "onProgressUpdate")
            apiCall =  APICall("http://carbon-emission-tracker-team-7.herokuapp.com/api/v1/tripinfo", "GET", email, password, result[0]as String)
            val result = apiCall.execute().get()
            val co2 = result.getJSONObject(0).getDouble("co2emissions")
            val trees = calculateTrees((co2*1000).toFloat() )
            val cO2View = findViewById<TextView>(R.id.co2calc_totalco2)
            cO2View.text = co2.toString()
            val treeOffSetView = findViewById<TextView>(R.id.co2calc_trees)
            treeOffSetView.text = trees.toString()
        }

        /**
         * The calculation done to produce the total CO2 is done in grams and kilometers, the distance is adgusted
         * to accomodate for that.
         *
         * @param distance this is the distance from the last location recorded by the device and the destination
         */
        private fun calculateCO2G(distance: Float): Float{
            Log.d("Calculator", "calculateCO2G")
            val distanceKM = distance / 1000.0f
            var vehicleEfficiency = 0.0f
            if(transportMode == "Car Diesel")
            {vehicleEfficiency = 121.5f}
            else if(transportMode == "Car Gas")
            {vehicleEfficiency = 123.4f}
            else if(transportMode == "Carpool (3) Diesel")
            {vehicleEfficiency = 40.5f}
            else if(transportMode == "Carpool (3) Gas")
            {vehicleEfficiency = 41.1f}
            else if(transportMode == "Public Transit")
            {vehicleEfficiency = 46.2f}
            else if(transportMode == "Walk")
            {vehicleEfficiency = 0.0f}
            else if(transportMode == "Bike")
            {vehicleEfficiency = 0.0f}
            return distanceKM * vehicleEfficiency
        }

        /**
         * ThisFormula assumes that any planted tree is harvested after a year and needs to be replaced
         * as a tree will absorb 59.7g CO2 per day, and the formula assumes that the user makes the trip everyday
         *
         */
        private fun calculateTrees(cO2InGrams: Float): Float
        {
            Log.d(TAG, "calculateTrees")
            val gramsPerDayPerTree = 59.7f
            return cO2InGrams / gramsPerDayPerTree/ 365.0f
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

    }
}
