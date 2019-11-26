package com.sustaincsej.sustain_cedricsebevanjean.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import com.sustaincsej.sustain_cedricsebevanjean.R
import android.widget.ArrayAdapter



class CO2CalcActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_co2_calc)

        showSpinner()
    }

    private fun showSpinner() {
        val travelModeSpinner = findViewById(R.id.co2calc_travelmode_spinner) as Spinner
        val dataAdapter = ArrayAdapter<String>(
            this, R.layout.simple_spinner, resources.getStringArray(R.array.newtrip_travel_modes))
        travelModeSpinner.adapter = dataAdapter
    }
}
