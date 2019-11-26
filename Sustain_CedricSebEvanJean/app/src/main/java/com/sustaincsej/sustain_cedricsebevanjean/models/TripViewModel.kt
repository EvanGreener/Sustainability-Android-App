package com.sustaincsej.sustain_cedricsebevanjean.models

import android.app.Application
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
    }


    fun insert(trip: Trip) = viewModelScope.launch {
        repository.insert(trip)
    }
}