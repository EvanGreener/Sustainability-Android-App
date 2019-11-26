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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.adapters.TripRecyclerViewAdapter
import com.sustaincsej.sustain_cedricsebevanjean.common.NewTripPopupFragment
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip
import com.sustaincsej.sustain_cedricsebevanjean.models.TripViewModel

class TripLogActivity : AppCompatActivity() {

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

        /*

        ===== UNCOMMENT when NewTripFragment is FULLY implemented

        val fab = findViewById<FloatingActionButton>(R.id.trip_log_add_trip)
        fab.setOnClickListener {
            val intent = Intent(this@TripLogActivity, NewTripPopupFragment::class.java)
            startActivityForResult(intent, newTripActivityRequestCode)
        }
        */


    }

    /*

    ===== UNCOMMENT when NewTripFragment is FULLY implemented

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newTripActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewTripPopupFragment.EXTRA_REPLY)?.let {
                val trip = Trip(it)
                tripViewModel.insert(trip)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

    */

    fun handleNewTripButtonClick(view: View) {
        NewTripPopupFragment(this).showPopup()
    }
}
