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
import com.sustaincsej.sustain_cedricsebevanjean.models.RemoteTripViewModel
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip
import com.sustaincsej.sustain_cedricsebevanjean.models.TripViewModel
import java.lang.Thread.sleep

/**
 * Class that will display all trips from the remote/php database.
 *
 * @author Cedric Richards
 * @author Sebastien Palin
 */
class RemoteTripLogActivity : AppCompatActivity() {


    private lateinit var tripViewModel: RemoteTripViewModel
    private val newTripActivityRequestCode = 1

    /**
     * onCreate that will set up the RecyclerView and fab
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_log)


        val recyclerView = findViewById<RecyclerView>(R.id.trip_log_recycler_view)
        val adapter = TripRecyclerViewAdapter(this, false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Connecting recycler view to DB and observing changes in DB
        tripViewModel = ViewModelProvider(this).get(RemoteTripViewModel::class.java)
        tripViewModel.allTrips.observe(this, Observer { trips ->
            // Update the cached copy of the words in the adapter.
            trips?.let { adapter.setTrips(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.trip_log_add_trip)
        fab.setOnClickListener {
            val intent = Intent(this, RemoteNewTripActivity::class.java)
            startActivityForResult(intent, newTripActivityRequestCode)
        }

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