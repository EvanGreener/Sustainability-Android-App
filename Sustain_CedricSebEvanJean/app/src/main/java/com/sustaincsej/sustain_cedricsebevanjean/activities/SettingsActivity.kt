package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.sustaincsej.sustain_cedricsebevanjean.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class that allows the user to input their settings via a form.
 *
 * @author Sebastien Palin
 * @author Jean Robatto
 * @author Evan Greenstein
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
     * onCreate that will also update the views based on what is in shared preferences and initialize all
     * lateinit variables.
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

        if (areSettingsFilled()) {
            savePreferences()
            updateUI()
            saveToast()
        }
        else {
            incompleteAlert()
        }
    }

    /**
     * Function that will check if all EditTexts have been filled. This is used to ensure that the user inputs
     * all necessary settings data and doesn't leave anything out.
     *
     * @return A boolean indicating if all fields have been filled
     */
    private fun areSettingsFilled() : Boolean {
        return firstName.text.toString() != "" && lastName.text.toString() != "" && email.text.toString() != ""
                && password.text.toString() != "" && homeLat.text.toString() != "" && homeLon.text.toString() != ""
                && schoolLat.text.toString() != "" && schoolLon.text.toString() != ""
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
     * Function that will return today's date. Used for storing the timestamp settings were saved.
     *
     * @return Today's date, formatted to dd/MM/yyy
     */
    private fun getDate() : String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val date = Calendar.getInstance().time
        return formatter.format(date)
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
     * function that displays a Toast informing the user that their settings have been saved
     */
    private fun saveToast() {
        Toast.makeText(this, getString(R.string.SaveToast), Toast.LENGTH_LONG).show()
    }

    /**
     * Function that displays an Alert telling the user they haven't filled every setting
     */
    private fun incompleteAlert() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.EmptyMessage))
            .setTitle(getString(R.string.EmptyTitle))
            .setPositiveButton(getString(R.string.EmptyButton), null)
            .create()
            .show()
    }

    /**
     * Override that will only go back if the user confirms they want to leave the settings page.
     */
    override fun onBackPressed() {
        Log.i(TAG, getString(R.string.BackClick))

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.LeaveTitle))
            .setMessage(getString(R.string.LeaveMessage))
            .setPositiveButton(getString(R.string.LeaveYes)) { _: DialogInterface, _: Int ->
                super.onBackPressed()
            }
            .setNegativeButton(getString(R.string.LeaveNo), null)
            .create()
            .show()
    }

    /**
     * Companion object used for logging
     */
    companion object {
        private val TAG = "SETTINGS"
    }


}
