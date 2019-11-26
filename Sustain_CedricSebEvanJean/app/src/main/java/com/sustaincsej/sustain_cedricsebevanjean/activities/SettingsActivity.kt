package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Context
import android.content.res.Resources
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

    /**
     * onCreate that will also update the views based on what is in shared preferences.
     */
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
     * Function that is called when the user clicks on the save button
     */
    fun handleSave(view: View) {
        Log.i(TAG, getString(R.string.SaveClick))

        savePreferences()
        updateUI()
    }

    /**
     * Function that will save all EditText input to shared preferences
     */
    private fun savePreferences() {
        with(getPreferences(Context.MODE_PRIVATE).edit()) {
            putString("FirstName", firstName.text.toString())
            putString("LastName", lastName.text.toString())
            putString("Email", email.text.toString())
            putString("Password", password.text.toString())
            putString("HomeLat", homeLat.text.toString())
            putString("HomeLon", homeLon.text.toString())
            putString("SchoolLat", schoolLat.text.toString())
            putString("SchoolLon", schoolLon.text.toString())
            putString("TimeStamp", getDate())
            commit()
        }
    }

    /**
     * Function that will update the UI when the user saves their settings
     */
    private fun updateUI() {
        val prefs = getPreferences(Context.MODE_PRIVATE)

        val dateText = prefs.getString("TimeStamp", "")
        dateStamp.text = getString(R.string.settings_last_modified_string) + " " + dateText
    }

    /**
     * Function that will return today's date. Used for storing the timestamp settings were saved.
     *
     * @return Today's date, formatted to dd/MM/yyy
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

    /**
     * Companion object used for logging
     */
    companion object {
        private val TAG = Resources.getSystem().getString(R.string.SettingsTag)
    }


}
