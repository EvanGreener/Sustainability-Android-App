package com.sustaincsej.sustain_cedricsebevanjean.models

import androidx.annotation.NonNull
import androidx.room.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity(tableName = "trip_table")
data class Trip (
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "travel_mode") @NonNull val travelMode: String,
    @ColumnInfo(name = "from_latitude") @NonNull val fromLatitude: Double,
    @ColumnInfo(name = "from_longitude") @NonNull val fromLongitude: Double,
    @ColumnInfo(name = "to_latitude") @NonNull val toLatitude: Double,
    @ColumnInfo(name = "to_longitude") @NonNull val toLongitude: Double,
    @NonNull val distance : Double,
    @ColumnInfo(name = "carbon_dioxide") @NonNull val carbonDioxide : Double,
    @ColumnInfo(name = "date_time_stamp")  @NonNull val dateTimeStamp: Date,
    @ColumnInfo(name = "reason_for_trip") @NonNull val reasonForTrip : String
)



class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
