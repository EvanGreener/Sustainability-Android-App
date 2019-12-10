package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.adapters.TripRecyclerViewAdapter
import com.sustaincsej.sustain_cedricsebevanjean.models.RemoteTripViewModel

/**
 * Shows a list of trips from the php api
 *
 * @author Cedric Richards
 */
class RemoteTripLogActivity : AppCompatActivity() {


    private lateinit var tripViewModel: RemoteTripViewModel
    private val newTripActivityRequestCode = 1

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
            finish()
        }

    }

    companion object {
        private val TAG = "RemoteTripLogActivity"
    }
}