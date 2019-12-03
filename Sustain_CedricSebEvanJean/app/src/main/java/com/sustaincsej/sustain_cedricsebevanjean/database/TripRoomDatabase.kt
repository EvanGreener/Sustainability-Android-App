package com.sustaincsej.sustain_cedricsebevanjean.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sustaincsej.sustain_cedricsebevanjean.models.Converters
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

@Database(entities = arrayOf(Trip::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TripRoomDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDAO

    //Prevents multiple instances of the database being open at the same time
    companion object {
        @Volatile
        private var INSTANCE: TripRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TripRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripRoomDatabase::class.java,
                    "trip_database"
                ).addCallback(TripDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class TripDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.tripDao())
                }
            }
        }

        suspend fun populateDatabase(tripDao: TripDAO) {
            /*
            //For testing purposes
            tripDao.deleteAll()

            //Sample trips for testing
            var trip = Trip(0,"BIKE",0.0,0.0,0.0,0.0,
                0.0,0.0, java.sql.Date.valueOf("2019-09-16") , "Idk")
            tripDao.insert(trip)
            trip = Trip(0,"GAS",1.0,9032.0,94.5,4.0,
                0.0,0.0, java.sql.Date.valueOf("2019-11-26") ,"Yeet")
            tripDao.insert(trip)
            */
        }
    }
}