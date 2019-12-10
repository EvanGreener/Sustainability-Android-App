package com.sustaincsej.sustain_cedricsebevanjean.models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.httprequests.APICall
import org.json.JSONArray
import java.util.*
import java.text.SimpleDateFormat
import java.text.ParseException


/**
 * A view model designed for remote trips
 */
class RemoteTripViewModel(application: Application) : AndroidViewModel(application) {

    var allTrips: LiveData<List<Trip>>

    init {
        val list :MutableList<Trip> = ArrayList()

        var email = ""
        var password = ""
        with(application.getSharedPreferences(application.getString(R.string.Preferences), Context.MODE_PRIVATE).all){

            email = this["Email"] as String
            password = this["Password"] as String

        }
        val jsonString = ""
        val apiCall = APICall("https://carbon-emission-tracker-team-7.herokuapp.com/api/v1/alltrips", "GET", email, password, jsonString)
        Log.d(TAG, "here")
        val results = apiCall.execute().get() as JSONArray
        Log.d(TAG, results.toString())
        for(i in 0 until results.length()){

            val jsonTrip = results.getJSONObject(i)
            val dateString = jsonTrip.getString("created_at")

            val inputDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            var date = Date()
            try {
                date = inputDate.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }


            val trip = Trip (
                jsonTrip.getInt("id"),
                jsonTrip.getString("mode"),
                jsonTrip.getJSONObject("from").getDouble("latitude"),
                jsonTrip.getJSONObject("from").getDouble("longitude"),
                jsonTrip.getJSONObject("to").getDouble("latitude"),
                jsonTrip.getJSONObject("to").getDouble("longitude"),
                jsonTrip.getDouble("distance"),
                jsonTrip.getDouble("co2emissions"),
                date,
                " "
            )
            list.add(trip)
        }
        allTrips = MutableLiveData(list)
    }

    companion object {
        private val TAG = "RemoteTripViewModel"
    }

}