package com.sustaincsej.sustain_cedricsebevanjean.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.adapters.TripRecyclerViewAdapter
import com.sustaincsej.sustain_cedricsebevanjean.common.NewTripPopupFragment
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip

class TripLogActivity : AppCompatActivity() {

    //For RecyclerView
    private lateinit var tripList: MutableList<Trip>
    private lateinit var tripView: RecyclerView
    private lateinit var tripViewAdapter: TripRecyclerViewAdapter
    private lateinit var tripViewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_log)

        initTripView()
    }

    fun handleNewTripButtonClick(view: View) {
        NewTripPopupFragment(this).showPopup()
    }

    private fun initTripView() {
        tripViewManager = LinearLayoutManager(this)
        //TODO The trip list should be a list of trip objects from the database
        tripList = mutableListOf(Trip(), Trip(), Trip(),Trip(), Trip(), Trip(),Trip(), Trip(), Trip())
        tripViewAdapter = TripRecyclerViewAdapter(tripList, this)
        tripView = findViewById<RecyclerView>(R.id.trip_log_recycler_view).apply {
            layoutManager = tripViewManager
            adapter = tripViewAdapter
        }

    }
}
