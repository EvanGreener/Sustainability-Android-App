package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import com.sustaincsej.sustain_cedricsebevanjean.R
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sustaincsej.sustain_cedricsebevanjean.business.Co2Calculator


class CO2CalcActivity : AppCompatActivity() {
    private lateinit var calculator: Co2Calculator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_co2_calc)

        showSpinner()
        Log.d("Calculator", "before calc")
        this.calculator = Co2Calculator(this)
        calculateDistanceTest()
    }
    private fun calculateDistanceTest(){
        var distanceTest = findViewById<TextView>(R.id.co2calc_totalco2)
        distanceTest.text = this.calculator.distanceToSchool().toString()
    }

    private fun showSpinner() {
        val travelModeSpinner = findViewById(R.id.co2calc_travelmode_spinner) as Spinner
        val dataAdapter = ArrayAdapter<String>(
            this, R.layout.simple_spinner, resources.getStringArray(R.array.newtrip_travel_modes))
        travelModeSpinner.adapter = dataAdapter
    }
}
