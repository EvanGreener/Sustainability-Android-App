package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton


import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.adapters.TripRecyclerViewAdapter
import com.sustaincsej.sustain_cedricsebevanjean.httprequests.APICall
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip
import com.sustaincsej.sustain_cedricsebevanjean.models.TripViewModel
import java.lang.Thread.sleep

class RemoteTripLogActivity : AppCompatActivity() {


    private lateinit var tripViewModel: TripViewModel
    private val newTripActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_log)


        val recyclerView = findViewById<RecyclerView>(R.id.trip_log_recycler_view)
        val adapter = TripRecyclerViewAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Connecting recycler view to DB and observing changes in DB
        tripViewModel = ViewModelProvider(this).get(TripViewModel::class.java)
        tripViewModel.allTrips.observe(this, Observer { trips ->
            // Update the cached copy of the words in the adapter.
            trips?.let { adapter.setTrips(it) }
        })
        retreiveFromAPI()

        val fab = findViewById<FloatingActionButton>(R.id.trip_log_add_trip)
        fab.setOnClickListener {
            val intent = Intent(this, RemoteNewTripActivity::class.java)
            startActivityForResult(intent, newTripActivityRequestCode)
        }

    }

    fun retreiveFromAPI()
    {
        Log.d(TAG, "retreiveFromApi")
        val jsonString = ""
        var apiCall = APICall("https://jayaghgtracker.herokuapp.com/api/v1/alltrips", "GET", "sect2team2@test", "1qazxsw2", jsonString)

        //val jsonString = "{\"fromlatitude\":45.4908788, \"fromlongitude\":-73.588405,\"tolatitude\":45.2908788,  \"tolongitude\":-73.588405,\"mode\":\"car\", \"engine\":\"gasoline\", \"consumption\":8.4}"
        //var apiCall =  APICall("https://jayaghgtracker.herokuapp.com/api/v1/addtrip", "POST", "sect2team2@test", "1qazxsw2", jsonString)
        var execute = apiCall.execute()
        sleep(10000)
        var results = execute.get()
        Log.i(TAG, results.toString())
    }

    companion object {
        private val TAG = "RemoteTripLogActivity"
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