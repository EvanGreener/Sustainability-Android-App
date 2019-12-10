package com.sustaincsej.sustain_cedricsebevanjean.adapters

import android.content.Context

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sustaincsej.sustain_cedricsebevanjean.R

import com.sustaincsej.sustain_cedricsebevanjean.models.TravelMode
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip
import com.sustaincsej.sustain_cedricsebevanjean.fragments.TripDialogFragment

class TripRecyclerViewAdapter internal constructor(
    context: Context,
    private val local: Boolean
) : RecyclerView.Adapter<TripRecyclerViewAdapter.TripViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var trips = emptyList<Trip>() // Cached copy of words

    inner class TripViewHolder(itemView: View, local: Boolean) : RecyclerView.ViewHolder(itemView) {
        var trip: Trip? = null

        val tripDateView: TextView = itemView.findViewById(R.id.trip_row_date)
        val tripDistanceView : TextView = itemView.findViewById(R.id.trip_row_distance)
        val tripCO2View : TextView = itemView.findViewById(R.id.trip_row_co2)
        val travelModeImg : ImageView = itemView.findViewById(R.id.trip_row_travelmode_pic)

        init {
            itemView.setOnClickListener{
                Log.i("TRIP_ROW", local.toString())
                val context = it.context
                val fragment = TripDialogFragment()
                val args = Bundle()
                args.putSerializable("trip", trip)
                args.putBoolean("local", local)
                fragment.arguments = args

                val app = context as AppCompatActivity
                fragment.show(app.supportFragmentManager, "TRIP DETAILS")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = inflater.inflate(R.layout.trip_row, parent, false)
        return TripViewHolder(itemView, local)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val current = trips[position]

        holder.trip = current
        holder.tripDateView.text = current.dateTimeStamp.toString()
        holder.tripDistanceView.text = current.distance.toString()
        holder.tripCO2View.text = current.carbonDioxide.toString()

        val travelMode = current.travelMode

        //Ill retrieve some sample images later
        when(travelMode){
            TravelMode.BIKE.name -> holder.travelModeImg.setImageResource(R.drawable.bike)
            TravelMode.CARPOOL_DIESEL.name -> holder.travelModeImg.setImageResource(R.drawable.cp_diesel)
            TravelMode.CAR_DIESEL.name -> holder.travelModeImg.setImageResource(R.drawable.diesel)
            TravelMode.CARPOOL_GAS.name -> holder.travelModeImg.setImageResource(R.drawable.gas)
            TravelMode.CAR_GAS.name -> holder.travelModeImg.setImageResource(R.drawable.gas)
            TravelMode.PUBLIC_TRANSIT.name -> holder.travelModeImg.setImageResource(R.drawable.bus)
            TravelMode.WALK.name -> holder.travelModeImg.setImageResource(R.drawable.walking_man)
            else -> holder.travelModeImg.setImageResource(R.drawable.co2)
            
        }
    }
    
    internal fun setTrips(trips: List<Trip>) {
        this.trips = trips
        notifyDataSetChanged()
    }

    override fun getItemCount() = trips.size
}