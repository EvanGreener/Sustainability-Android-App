package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Context
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class CO2CalcActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {
    private lateinit var calculator: Co2Calculator
    private lateinit var transportMode : String
    //if false destination is school
    private var destinationHome = false
    private var vehicleEfficiency = 123.4f

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Calculator", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_co2_calc)
        this.calculator = Co2Calculator()
        this.calculator.execute()
        showSpinner()
        setUpToggleButton()
        Log.d("Calculator", "before calc")


    }
    private fun setUpToggleButton()
    {
        Log.d("Calculator", "setUpToggleButton")
        val toggle: ToggleButton = findViewById(R.id.co2calc_homeschool_toggle)
        toggle.setOnCheckedChangeListener { _, isHome ->
            //if false destination is school
                destinationHome = isHome
            Log.i("Calculator", destinationHome.toString())
            this.calculator = Co2Calculator()
            this.calculator.execute()
        }

    }


    private fun showSpinner() {
        Log.d("Calculator", "ShowSpinner")
        val travelModeSpinner = findViewById(R.id.co2calc_travelmode_spinner) as Spinner
        val dataAdapter = ArrayAdapter<String>(
            this, R.layout.simple_spinner, resources.getStringArray(R.array.newtrip_travel_modes))
        travelModeSpinner.adapter = dataAdapter
        travelModeSpinner.onItemSelectedListener = this

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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
        this.calculator.execute()
    }

    private inner class Co2Calculator : AsyncTask<Void, Float, Float>() {

        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private lateinit var currentLocation: Location
        private var haveLocation = false
        private var schoolLongitude = -73.58854
        private var schoolLatutude = 45.48775
        private var homeLongitude = 0.0
        private var homeLatutude = 0.0

        override fun doInBackground(vararg unused: Void): Float{
            Log.d("Calculator", "initializing calc")


            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@CO2CalcActivity)
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
                        publishProgress(distanceToSchool())
                        Log.d("Calculator", "here1")
                    }
                    Log.d("Calculator", "here2")
                }


            Log.d("Calculator", "execute ended")
            return 0.0f
        }
        override fun onProgressUpdate(vararg result: Float?){
            Log.d("Calculator", "onProgressUpdate")
            var cO2View = findViewById<TextView>(R.id.co2calc_totalco2)
            var totalCO2 = calculateCO2G(result[0] as Float)
            cO2View.text = totalCO2.toString()
            var treeOffSetView = findViewById<TextView>(R.id.co2calc_trees)
            var treeOffSet = calculateTrees(totalCO2)
            treeOffSetView.text = treeOffSet.toString()
        }

        private fun calculateCO2G(distance: Float): Float{
            Log.d("Calculator", "calculateCO2G")
            var distanceKM = distance / 1000.0f
            var totalCO2 = distanceKM * this@CO2CalcActivity.vehicleEfficiency
            return totalCO2
        }
        private fun calculateTrees(cO2InGrams: Float): Float
        {
            Log.d("Calculator", "calculateTrees")
            val gramsPerDayPerTree = 59.7f
            var treesToOffset = cO2InGrams / gramsPerDayPerTree
            return treesToOffset
        }

        fun distanceToSchool() : Float
        {
            Log.d("Calculator", "distanceToSchool")
            var results: FloatArray = floatArrayOf(1.0f)
            Log.d("Calculator", "location = " +currentLocation.toString())
            if(this.haveLocation) {
                Log.d("Calculator", "in if")
                if(this@CO2CalcActivity.destinationHome) {

                    Location.distanceBetween(
                        this.currentLocation.latitude,
                        this.currentLocation.longitude,
                        homeLatutude,
                        homeLongitude,
                        results
                    )
                }
                else
                {
                    Location.distanceBetween(
                        this.currentLocation.latitude,
                        this.currentLocation.longitude,
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
        override fun onPreExecute() {
            // set up the task here
            Log.d("Calculator", "onPreExecute")
            with(getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE).all){
                var schoolLat = this["SchoolLat"] as String
                var schoolLon = this["SchoolLon"] as String
                var homeLat = this["HomeLat"] as String
                var homeLon = this["HomeLon"] as String
                schoolLongitude = schoolLon.toDouble()
                schoolLatutude = schoolLat.toDouble()
                homeLongitude = homeLon.toDouble()
                homeLatutude = homeLat.toDouble()
            }
        }

    }
}
