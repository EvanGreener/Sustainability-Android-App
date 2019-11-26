package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.sustaincsej.sustain_cedricsebevanjean.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class that allows the user to input their settings via a form.
 */
class SettingsActivity : AppCompatActivity() {

    //All form elements
    private lateinit var firstName : EditText
    private lateinit var lastName : EditText
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var homeLat : EditText
    private lateinit var homeLon : EditText
    private lateinit var schoolLat : EditText
    private lateinit var schoolLon : EditText
    //To display the date stamp
    private lateinit var dateStamp : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        getEditTexts()
        dateStamp = findViewById(R.id.settings_date_stamp)

        updateUI()
    }

    /**
     * Function that will get all the user input fields and put them into private variables.
     */
    private fun getEditTexts() {
        firstName = findViewById<EditText>(R.id.settings_firstname) as EditText
        lastName = findViewById<EditText>(R.id.settings_firstname) as EditText
        email = findViewById<EditText>(R.id.settings_firstname) as EditText
        password = findViewById<EditText>(R.id.settings_firstname) as EditText
        homeLat = findViewById<EditText>(R.id.settings_firstname) as EditText
        homeLon = findViewById<EditText>(R.id.settings_firstname) as EditText
        schoolLat = findViewById<EditText>(R.id.settings_firstname) as EditText
        schoolLon = findViewById<EditText>(R.id.settings_firstname) as EditText
    }

    /**
     * Function that will save all EditText input to shared preferences
     */
    fun savePreferences(view: View) {
        with(getPreferences(Context.MODE_PRIVATE).edit()) {
            putString("TimeStamp", getDate())
            commit()
        }
        updateUI()
    }

    /**
     * Function that will update the UI when the user saves their settings
     */
    private fun updateUI() {
        val prefs = getPreferences(Context.MODE_PRIVATE)
        dateStamp.text = prefs.getString("TimeStamp", getString(R.string.settings_last_modified_string))
    }

    /**
     * Function that will return today's date. Used for storing the timestamp settings were saved.
     */
    private fun getDate() : String {
        //Today's date
        val formater = SimpleDateFormat("dd/MM/yyyy")
        val date = Calendar.getInstance().time
        return formater.format(date)
    }

    //Handle back button press
    override fun onBackPressed() {
        super.onBackPressed()
    }




}
