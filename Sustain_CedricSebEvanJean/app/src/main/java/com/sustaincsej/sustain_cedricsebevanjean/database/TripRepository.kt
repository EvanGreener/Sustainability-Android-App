package com.sustaincsej.sustain_cedricsebevanjean.database

import androidx.lifecycle.LiveData
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip

class TripRepository(private val tripDao: TripDAO) {


    val allTrips: LiveData<List<Trip>> = tripDao.getAllTrips()

    suspend fun insert(trip: Trip) {
        tripDao.insert(trip)
    }

    fun delete (trip: Trip) {
        tripDao.delete(trip)
    }
}