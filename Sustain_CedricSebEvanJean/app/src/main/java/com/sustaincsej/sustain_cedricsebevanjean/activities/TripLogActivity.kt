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

class TripLogActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tripViewModel: TripViewModel
    private val newTripActivityRequestCode = 1

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
            val intent = Intent(this, NewTripActivity::class.java)
            startActivityForResult(intent, newTripActivityRequestCode)
        }
    }

    /**
     * If the activity was started from Home/School trip, add trip
     */
    override fun onStart() {
        super.onStart()

        val preset = intent.getStringExtra("preset") ?: ""

        if (preset == "home") {
            val intent = Intent(this, NewTripActivity::class.java)
            intent.putExtra("preset", "home")
            startActivityForResult(intent, newTripActivityRequestCode)
        } else if (preset == "school") {
            val intent = Intent(this, NewTripActivity::class.java)
            intent.putExtra("preset", "school")
            startActivityForResult(intent, newTripActivityRequestCode)
        }

        intent.removeExtra("preset")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newTripActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras

            var lat = extras?.getDouble(NewTripActivity.TO_LAT)
            var lon = extras?.getDouble(NewTripActivity.TO_LON)
            val travel_mode = extras?.getString(NewTripActivity.TRAVEL_MODE)!!
            val reason = extras.getString(NewTripActivity.REASON)!!
            val distance = extras.getDouble(NewTripActivity.DISTANCE)
            val co2 = extras.getDouble(NewTripActivity.CO2)
            val startLat = extras.getDouble(NewTripActivity.FROM_LAT)
            val startLon = extras.getDouble(NewTripActivity.FROM_LON)

            Log.d(TAG, "co2: $co2")
            Log.d(TAG, "distance: $distance")

            val jsonString = "{\"fromlatitude\":\"45\", \"fromlongitude\":\"-73\", \"tolatitude\":\"45.001\", \"tolongitude\":\"-73.001\", \"mode\":\"car\"}"
            val request = APICall("https://jayaghgtracker.herokuapp.com/api/v1/tripinfo", "GET", "sect2team2@test", "1qazxsw2", jsonString)
            val response = request.execute().get()

            //val jsonString = "{\"fromlatitude\":\"45.4908788\", \"fromlongitude\":\"-73.588405\",\"tolatitude\":\"45.2908788\",  \"tolongitude\":\"-73.588405\",\"mode\":\"car\", \"engine\":\"gasoline\", \"consumption\":\"8.4\"}"
            //var apiCall = APICall("http://carbon-emission-tracker-team-7.herokuapp.com/api/v1/tripinfo", "GET", "robatto.jeanmarie@gmail.com", "password", jsonString)
            //var execute = apiCall.execute().get()
            Log.d(TAG, response.toString())

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

    companion object{
        val TAG = "TripLogActivity"
    }



}
