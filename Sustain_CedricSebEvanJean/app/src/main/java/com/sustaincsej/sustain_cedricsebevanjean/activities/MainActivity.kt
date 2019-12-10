package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.sustaincsej.sustain_cedricsebevanjean.R

/**
 * Activity that displays the "Main Menu" of the app, where the user can navigate to each sub-menu/
 * feature from.
 *
 * @author Sebastien Palin
 * @author Jean Robatto
 * @author Cedric Richards
 * @author Evan Greenstein
 */
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
        storeLocationSharedPref()
    }

    /**
     * Function that stores the user's current location inside shared preferences.
     */
    private fun storeLocationSharedPref()
    {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.d("Calculator", "fusedLocation init")
        //Asyncronous call to get location the method will end before it completes its task.
        val locationRequest = LocationRequest.create()
        val locationCallback = LocationCallback()
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->

                if (location != null)
                {
                    with(getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE).edit()) {
                        putString("CurrentLongitude", location.longitude.toString())
                        putString("CurrentLatitude", location.latitude.toString())
                        apply()
                    }
                }
                else
                {
                    with(getSharedPreferences(getString(R.string.Preferences), Context.MODE_PRIVATE).edit()) {
                        putString("CurrentLongitude", "0.0")
                        putString("CurrentLatitude", "0.0")
                        apply()
                    }
                }
                Log.i("Location", location!!.longitude.toString())
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

    /**
     * Click handler that makes an intent to go to the WeatherActivity.
     *
     * @param view
     */
    fun handleWeatherAPIClick(view: View) {
        startActivity(Intent(this, WeatherActivity::class.java))
    }

    /**
     * Click handler that makes an intent to go to the SustainabilityFactsActivity.
     *
     * @param view
     */
    fun handleSustFactClick(view: View) {
        startActivity(Intent(this, SustainabilityFactsActivity::class.java))
    }

    /**
     * Click handler that makes an intent to go to the TripLogActivity.
     *
     * @param view
     */
    fun handleTripLogClick(view: View) {
        startActivity(Intent(this, TripLogActivity::class.java))
    }

    /**
     * Click handler that makes an intent to go to the TripLogActivity for a to school trip.
     *
     * @param view
     */
    fun handleSchoolTripClick(view: View) {
        val intent = Intent(this, TripLogActivity::class.java)
        intent.putExtra("preset", "school")
        startActivity(intent)
    }

    /**
     * Click handler that makes an intent to go to the TripLogActivity for a to home trip.
     *
     * @param view
     */
    fun handleHomeTripClick(view: View) {
        val intent = Intent(this, TripLogActivity::class.java)
        intent.putExtra("preset", "home")
        startActivity(intent)
    }

    /**
     * Click handler that makes an intent to go to the CO2CalcActivity.
     *
     * @param view
     */
    fun handleCO2Click(view: View) {
        startActivity(Intent(this, CO2CalcActivity::class.java))
    }

    /**
     * Click handler that makes an intent to go to the RemoteTripLogActivity.
     *
     * @param view
     */
    fun handleRemoteTripLogClick(view: View) {
        startActivity(Intent(this, RemoteTripLogActivity::class.java))
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

    // For logging
    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }
}