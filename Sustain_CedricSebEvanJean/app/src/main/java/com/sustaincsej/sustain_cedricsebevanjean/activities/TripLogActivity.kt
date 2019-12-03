package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip
import com.sustaincsej.sustain_cedricsebevanjean.models.TripViewModel

class TripLogActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tripViewModel: TripViewModel
    private val newTripActivityRequestCode = 1

    //For RecyclerView
    private lateinit var tripList: MutableList<Trip>
    private lateinit var tripView: RecyclerView
    private lateinit var tripViewAdapter: TripRecyclerViewAdapter
    private lateinit var tripViewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_log)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@TripLogActivity)

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



        val fab = findViewById<FloatingActionButton>(R.id.trip_log_add_trip)
        fab.setOnClickListener {
            val intent = Intent(this, NewTripPopupFragment::class.java)
            startActivityForResult(intent, newTripActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newTripActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            var lat = extras?.getDouble(NewTripPopupFragment.TO_LAT)
            var lon = extras?.getDouble(NewTripPopupFragment.TO_LON)
            val travel_mode = extras?.getString(NewTripPopupFragment.TRAVEL_MODE)!!
            val reason = extras?.getString(NewTripPopupFragment.REASON)!!
            val distance = extras?.getDouble(NewTripPopupFragment.DISTANCE)
            val co2 = extras?.getDouble(NewTripPopupFragment.CO2)
            val startLat = extras?.getDouble(NewTripPopupFragment.FROM_LAT)
            val startLon = extras?.getDouble(NewTripPopupFragment.FROM_LON)



            val trip  = Trip(id = 0, travelMode = travel_mode, reasonForTrip = reason, distance = distance,
                carbonDioxide = co2, dateTimeStamp = java.util.Calendar.getInstance().time, fromLatitude = startLat,
                fromLongitude = startLon, toLatitude = lat!!, toLongitude = lon!! )
            tripViewModel.insert(trip)

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }





}
