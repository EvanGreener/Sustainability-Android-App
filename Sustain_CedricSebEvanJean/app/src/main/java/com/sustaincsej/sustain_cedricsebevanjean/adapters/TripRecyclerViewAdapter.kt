package com.sustaincsej.sustain_cedricsebevanjean.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.activities.TripActivity
import com.sustaincsej.sustain_cedricsebevanjean.models.Trip

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

    /**
     * Creates the viewholder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val tripRow = LayoutInflater.from(parent.context).inflate(R.layout.trip_row, parent, false)
        return ViewHolder(tripRow)
    }

    /**
     * Adds data to row
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO Add correct text for date/distance/co2 and correct image for travelmode
        holder.co2.text = data[position].co2
    }

    /**
     * Return count
     */
    override fun getItemCount(): Int {
        return data.size
    }

}