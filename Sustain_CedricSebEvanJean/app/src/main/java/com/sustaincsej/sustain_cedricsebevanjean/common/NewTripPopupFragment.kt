package com.sustaincsej.sustain_cedricsebevanjean.common

import android.app.Activity
import android.app.Dialog
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sustaincsej.sustain_cedricsebevanjean.R

class NewTripPopupFragment(private var context: Activity) {

    private val dialog = Dialog(context)

    init {
        //Prepare dialog
        dialog.setContentView(R.layout.dialog_fragment_new_trip)

        //Set spinner values
        val spinner = dialog.findViewById(R.id.newtrip_travelmode_spinner) as Spinner
        val adapter = ArrayAdapter(context,
            R.layout.simple_spinner, context.resources.getStringArray(R.array.newtrip_travel_modes))
        spinner.adapter = adapter

        //Set button click events
        dialog.findViewById<Button>(R.id.newtrip_local_btn).setOnClickListener {
            onClick(it.id)
        }
        dialog.findViewById<Button>(R.id.newtrip_remote_btn).setOnClickListener {
            onClick(it.id)
        }
        dialog.findViewById<FloatingActionButton>(R.id.newtrip_close_popup_btn).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun onClick(id: Int) {
        val lat = dialog.findViewById<EditText>(R.id.newtrip_tolat).text.toString().toDouble()
        val lon = dialog.findViewById<EditText>(R.id.newtrip_tolon).text.toString().toDouble()
        val travelmode = dialog.findViewById<Spinner>(R.id.newtrip_travelmode_spinner).selectedItem.toString()
        val reason = dialog.findViewById<EditText>(R.id.newtrip_reason).text.toString()

        when (id) { //TODO Implement buttons
            R.id.newtrip_remote_btn -> Log.i(TAG, "Remote button clicked")//FIRE REMOTE
            R.id.newtrip_local_btn ->Log.i(TAG, "Local button clicked")//FIRE LOCAL
        }
    }

    fun showPopup() {
        dialog.show()
    }

    companion object {
        private val TAG = "NewTripPopupFragment"
    }

}