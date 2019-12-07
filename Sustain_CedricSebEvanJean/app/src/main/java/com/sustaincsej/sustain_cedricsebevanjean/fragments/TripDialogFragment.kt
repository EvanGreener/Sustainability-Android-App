package com.sustaincsej.sustain_cedricsebevanjean.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.activities.TripLogActivity
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip
import com.sustaincsej.sustain_cedricsebevanjean.models.TripViewModel

class TripDialogFragment : DialogFragment() {

    private lateinit var trip: Trip
    private lateinit var _context: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_trip, container, false)
        _context = root.context
        trip = arguments!!.getSerializable("trip") as Trip

        //Set text
        root.findViewById<TextView>(R.id.trip_fromlat).text = trip.fromLatitude.toString()
        root.findViewById<TextView>(R.id.trip_fromlon).text = trip.fromLongitude.toString()
        root.findViewById<TextView>(R.id.trip_tolat).text = trip.toLatitude.toString()
        root.findViewById<TextView>(R.id.trip_tolon).text = trip.toLongitude.toString()
        root.findViewById<TextView>(R.id.trip_travelmode).text = trip.travelMode
        root.findViewById<TextView>(R.id.trip_distance).text = trip.distance.toString()
        root.findViewById<TextView>(R.id.trip_co2).text = trip.carbonDioxide.toString()
        root.findViewById<TextView>(R.id.trip_date).text = trip.dateTimeStamp.toString()
        root.findViewById<TextView>(R.id.trip_reason).text = trip.reasonForTrip

        root.findViewById<FloatingActionButton>(R.id.trip_close_popup_btn).setOnClickListener {
            dismiss()
        }

        root.findViewById<Button>(R.id.delete_trip_button).setOnClickListener{
            ViewModelProvider(this).get(TripViewModel::class.java).delete(trip)
            startActivity(Intent(_context, TripLogActivity::class.java))
        }

        return root
    }

    companion object {
        private val TAG = "TripDialogFragment"
    }
}
