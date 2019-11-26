package com.sustaincsej.sustain_cedricsebevanjean.adapters

import android.content.Context

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sustaincsej.sustain_cedricsebevanjean.R

import com.sustaincsej.sustain_cedricsebevanjean.models.TravelMode
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip
import com.sustaincsej.sustain_cedricsebevanjean.activities.TripActivity

class TripRecyclerViewAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<TripRecyclerViewAdapter.TripViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var trips = emptyList<Trip>() // Cached copy of words

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tripDateView: TextView = itemView.findViewById(R.id.trip_row_date)
        val tripDistanceView : TextView = itemView.findViewById(R.id.trip_row_distance)
        val tripCO2View : TextView = itemView.findViewById(R.id.trip_row_co2)
        val travelModeImg : ImageView = itemView.findViewById(R.id.trip_row_travelmode_pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = inflater.inflate(R.layout.trip_row, parent, false)
        return TripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val current = trips[position]
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

/*
class TripRecyclerViewAdapter(private val data: MutableList<Trip>, private val context: Activity):
    RecyclerView.Adapter<TripRecyclerViewAdapter.ViewHolder>() {

    //View Holder Class
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        internal var distance: TextView
        internal var date: TextView
        internal var co2: TextView
        internal var image: ImageView

        init {
            distance = itemView.findViewById(R.id.trip_row_distance)
            date = itemView.findViewById(R.id.trip_row_date)
            co2 = itemView.findViewById(R.id.trip_row_co2)
            image = itemView.findViewById(R.id.trip_row_travelmode_pic)

            itemView.setOnClickListener {
                //TODO Pass Trip Object as extra
                context.startActivity(Intent(context, TripActivity::class.java))
            }
        }

    }
    */
    
    
    /**
     * Creates the viewholder
     */
    /*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val tripRow = LayoutInflater.from(parent.context).inflate(R.layout.trip_row, parent, false)
        return ViewHolder(tripRow)
    }
    */
    /**
     * Adds data to row
     */
     
    /*
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO Add correct text for date/distance/co2 and correct image for travelmode
        holder.co2.text = data[position].co2
    }
    */

    /**
     * Return count
     */
     /*
    override fun getItemCount(): Int {
        return data.size
    }
    */

