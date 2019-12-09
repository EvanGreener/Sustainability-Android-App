package com.sustaincsej.sustain_cedricsebevanjean.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip

@Dao
interface TripDAO {

    @Insert
    suspend fun insert(trip: Trip)

    @Query("SELECT * FROM trip_table ORDER BY date_time_stamp DESC")
    fun getAllTrips() : LiveData<List<Trip>>

    @Delete
    suspend fun  delete(trip: Trip)

    @Query("DELETE FROM trip_table")
    suspend fun deleteAll()

}