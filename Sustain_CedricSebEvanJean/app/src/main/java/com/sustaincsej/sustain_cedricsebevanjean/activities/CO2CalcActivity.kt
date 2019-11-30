package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.app.Activity
import android.content.Context
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import com.sustaincsej.sustain_cedricsebevanjean.R
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sustaincsej.sustain_cedricsebevanjean.business.Co2Calculator


class CO2CalcActivity : AppCompatActivity() {
    private lateinit var calculator: Co2Calculator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_co2_calc)

        showSpinner()
        Log.d("Calculator", "before calc")
        this.calculator = Co2Calculator()
        this.calculator.execute()
    }


    private fun showSpinner() {
        val travelModeSpinner = findViewById(R.id.co2calc_travelmode_spinner) as Spinner
        val dataAdapter = ArrayAdapter<String>(
            this, R.layout.simple_spinner, resources.getStringArray(R.array.newtrip_travel_modes))
        travelModeSpinner.adapter = dataAdapter
    }

    private inner class Co2Calculator : AsyncTask<Void, Float, Float>() {

        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private lateinit var currentLocation: Location
        private var haveLocation = false
        private val dawsonLongitude = -73.58854
        private val dawsonLatitude = 45.48775


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
                    }

                }



            return 0.0f
        }
        override fun onProgressUpdate(vararg result: Float?){
            var distanceTest = findViewById<TextView>(R.id.co2calc_totalco2)
            distanceTest.text = result[0].toString()
        }

//        private fun calculateDistanceTest(){
//            var distanceTest = findViewById<TextView>(R.id.co2calc_totalco2)
//            distanceTest.text = distanceToSchool().toString()
//        }
        fun distanceToSchool() : Float
        {
            var results: FloatArray = floatArrayOf(1.0f)
            Log.d("Calculator", "location = " +currentLocation.toString())
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
        override fun onPreExecute() {
            // set up the task here

        }

    }
}
