package com.sustaincsej.sustain_cedricsebevanjean.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sustaincsej.sustain_cedricsebevanjean.models.Converters
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip

@Database(entities = arrayOf(Trip::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TripRoomDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDAO

    //Prevents multiple instances of the database being open at the same time
    companion object {
        @Volatile
        private var INSTANCE: TripRoomDatabase? = null

        fun getDatabase(context: Context): TripRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripRoomDatabase::class.java,
                    "trip_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}