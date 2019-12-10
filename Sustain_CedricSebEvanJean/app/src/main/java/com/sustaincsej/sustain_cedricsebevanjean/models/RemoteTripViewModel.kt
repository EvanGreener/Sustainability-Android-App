package com.sustaincsej.sustain_cedricsebevanjean.models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.database.TripRepository
import com.sustaincsej.sustain_cedricsebevanjean.database.TripRoomDatabase
import com.sustaincsej.sustain_cedricsebevanjean.httprequests.APICall
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.ParsePosition
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_INSTANT
import java.util.*



class RemoteTripViewModel(application: Application) : AndroidViewModel(application) {

    var allTrips: LiveData<List<Trip>>

    init {
        var list :MutableList<Trip> = ArrayList()

        var date = Date()

        var email = ""
        var password = ""
        with(application.getSharedPreferences(application.getString(R.string.Preferences), Context.MODE_PRIVATE).all){

            email = this["Email"] as String
            password = this["Password"] as String

        }
        val jsonString = ""
        var apiCall = APICall("http://carbon-emission-tracker-team-7.herokuapp.com/api/v1/alltrips", "GET", email, password, jsonString)
        Log.d(TAG, "here")
        var results = apiCall.execute().get() as JSONArray
        Log.d(TAG, results.toString())
        for(i in 0 until results.length()){

            val jsonTrip = results.getJSONObject(i)
            var dateString = jsonTrip.getString("created_at")
            var date = Date()
            date.setHours(dateString.substring(11, 12).toInt())
            date.setMinutes(dateString.substring(14, 15).toInt())
            date.setMonth(dateString.substring(5, 6).toInt())
            date.setSeconds(dateString.substring(17, 18).toInt())
            date.setYear(dateString.substring(0, 3).toInt())
            date.setDate(dateString.substring(8, 9).toInt())

            val trip = Trip (
                jsonTrip.getInt("id"),
                jsonTrip.getString("mode"),
                jsonTrip.getJSONObject("from").getDouble("latitude"),
                jsonTrip.getJSONObject("from").getDouble("longitude"),
                jsonTrip.getJSONObject("to").getDouble("latitude"),
                jsonTrip.getJSONObject("to").getDouble("longitude"),
                jsonTrip.getDouble("distance"),
                jsonTrip.getDouble("co2emissions"),
                //DateFormat.getInstance().parse(jsonTrip.getString("created_at")) as Date
                Date(),
                " "
            )
            list.add(trip)
        }
        allTrips = MutableLiveData<List<Trip>>(list)
        //ISO_INSTANT


    }

    fun insert(trip: Trip) = viewModelScope.launch {
    //    repository.insert(trip)
    }

    fun delete(trip: Trip) = viewModelScope.launch {
        //repository.delete(trip)
    }

    companion object {
        private val TAG = "RemoteTripViewModel"
    }

}