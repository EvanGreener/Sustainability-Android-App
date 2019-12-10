package com.sustaincsej.sustain_cedricsebevanjean.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sustaincsej.sustain_cedricsebevanjean.database.TripRepository
import com.sustaincsej.sustain_cedricsebevanjean.database.TripRoomDatabase
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TripRepository

    val allTrips: LiveData<List<Trip>>

    init {

        val tripsDao = TripRoomDatabase.getDatabase(application, viewModelScope).tripDao()
        repository = TripRepository(tripsDao)
        allTrips = repository.allTrips
        Log.d("Trips", "allTrips $allTrips")
        Log.d("Trips", "allTrips.value "+allTrips.value.toString())
    }


    fun insert(trip: Trip) = viewModelScope.launch {
        repository.insert(trip)
    }

    fun delete(trip: Trip) = viewModelScope.launch {
        repository.delete(trip)
    }
}