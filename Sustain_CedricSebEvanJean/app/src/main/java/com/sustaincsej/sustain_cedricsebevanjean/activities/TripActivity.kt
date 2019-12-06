package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.database.TripDAO_Impl
import com.sustaincsej.sustain_cedricsebevanjean.database.TripRepository
import com.sustaincsej.sustain_cedricsebevanjean.database.TripRoomDatabase
import com.sustaincsej.sustain_cedricsebevanjean.database.TripRoomDatabase_Impl
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip
import com.sustaincsej.sustain_cedricsebevanjean.models.TripViewModel
import kotlinx.coroutines.CoroutineScope

class TripActivity : AppCompatActivity() {

    private lateinit var trip: Trip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        trip = intent.getSerializableExtra("trip") as Trip
    }

    override fun onStart() {
        super.onStart()

        //Set text
        findViewById<TextView>(R.id.trip_fromlat).text = trip.fromLatitude.toString()
        findViewById<TextView>(R.id.trip_fromlon).text = trip.fromLongitude.toString()
        findViewById<TextView>(R.id.trip_tolat).text = trip.toLatitude.toString()
        findViewById<TextView>(R.id.trip_tolon).text = trip.toLongitude.toString()
        findViewById<TextView>(R.id.trip_travelmode).text = trip.travelMode
        findViewById<TextView>(R.id.trip_distance).text = trip.distance.toString()
        findViewById<TextView>(R.id.trip_co2).text = trip.carbonDioxide.toString()
        findViewById<TextView>(R.id.trip_date).text = trip.dateTimeStamp.toString()
        findViewById<TextView>(R.id.trip_reason).text = trip.reasonForTrip
    }

    fun onDeleteClick(view: View) {
        ViewModelProvider(this).get(TripViewModel::class.java).delete(trip)
        startActivity(Intent(this, TripLogActivity::class.java))
    }

    companion object {
        private val TAG = "TripActivity"
    }
}
