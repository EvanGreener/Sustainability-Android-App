package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.common.NewTripPopupFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get permission
        val permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,0)

        //Check if shared preferences exist
        if (! doPreferencesExist()) {
            Log.i(TAG, getString(R.string.NoPrefs))
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    /**
     * Function that will check if all shared preferences exist.
     *
     * Checks every preference since we need every one of them and in case all the preferences aren't
     * saved for whatever reason.
     *
     * @return A boolean indicating if all preferences exist
     */
    private fun doPreferencesExist() : Boolean {
        val prefs = getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE)
        return prefs.contains("FirstName") && prefs.contains("LastName") && prefs.contains("Email")
                && prefs.contains("Password") && prefs.contains("HomeLat") && prefs.contains("HomeLon")
                && prefs.contains("SchoolLat") && prefs.contains("SchoolLon") && prefs.contains("TimeStamp")
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

    /**
     * Method to inflate the options menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)
        return true
    }

    /**
     * Function that will launch one of the options activities when one
     * of the options are clicked
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.MenuAbout -> {
                aboutClicked()
                true
            }
            R.id.MenuWWW -> {
                wwwClicked()
                true
            }
            R.id.MenuSettings -> {
                settingsClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Function that will create an intent to go to the About activity
     */
    private fun aboutClicked() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    /**
     * Function that will create an intent to go to the Dawson Sustain
     * website.
     */
    private fun wwwClicked() {
        val webpage: Uri = Uri.parse(getString(R.string.DawsonURL))
        startActivity(Intent(Intent.ACTION_VIEW, webpage))
    }

    /**
     * Function that will create an intent to go to the Settings activity.
     */
    private fun settingsClicked() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val TAG = "MAIN_ACTIVITY"
    }
}