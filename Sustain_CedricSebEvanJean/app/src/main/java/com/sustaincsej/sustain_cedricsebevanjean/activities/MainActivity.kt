package com.sustaincsej.sustain_cedricsebevanjean.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.sustaincsej.sustain_cedricsebevanjean.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Method to inflate the options menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)
        return true
    }

    /**
     * Function that will launch one of the options activities when one
     * of the options are clicked
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.MenuAbout -> {
                aboutClicked()
                true
            }
            R.id.MenuWWW -> {
                wwwClicked()
                true
            }
            R.id.MenuSettings -> {
                settingsClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Function that will create an intent to go to the About activity
     */
    private fun aboutClicked() {

    }

    /**
     * Function that will create an intent to go to the Dawson Sustain
     * website.
     */
    private fun wwwClicked() {

    }

    /**
     * Function that will create an intent to go to the Settings activity.
     */
    private fun settingsClicked() {

    }
}