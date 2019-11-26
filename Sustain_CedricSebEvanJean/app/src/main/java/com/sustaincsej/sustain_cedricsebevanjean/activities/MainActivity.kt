package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.adapters.TripRecyclerViewAdapter
import com.sustaincsej.sustain_cedricsebevanjean.common.NewTripPopupFragment
import com.sustaincsej.sustain_cedricsebevanjean.models.TripViewModel

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get permission
        val permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,0)

    }

    fun handleWeatherAPIClick(view: View) {
        startActivity(Intent(this, WeatherActivity::class.java))
    }

    fun handleSustFactClick(view: View) {
        startActivity(Intent(this, SustainabilityFactsActivity::class.java))
    }
    
    fun handleTripLogClick(view: View) {
        startActivity(Intent(this, TripLogActivity::class.java))
    }

    fun handleSchoolTripClick(view: View) {
        NewTripPopupFragment(this).showPopup()
    }

    fun handleHomeTripClick(view: View) {
        NewTripPopupFragment(this).showPopup()
    }

    fun handleCO2Click(view: View) {
        startActivity(Intent(this, CO2CalcActivity::class.java))
    }
}
