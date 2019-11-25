package com.sustaincsej.sustain_cedricsebevanjean.shared

import android.content.Intent
import android.net.Uri
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.activities.AboutActivity
import com.sustaincsej.sustain_cedricsebevanjean.activities.SettingsActivity

/**
 * Class that is dedicated to making the correct intent go along with an options menu selection.
 *
 *
 */
class MenuSelector {

    /**
     * Function that will launch an intent based on which option menu itemId was passed to it.
     *
     * @param itemId The itemId of the item
     */
    fun selectIntent(itemId: Int) {

    }

    /**
     * Function that will create an intent to go to the About activity
     */
    private fun aboutClicked() {
        val intent = Intent(this, AboutActivity::class.java)
        return intent
    }

    /**
     * Function that will create an intent to go to the Dawson Sustain
     * website.
     */
    private fun wwwClicked() {
        val webpage: Uri = Uri.parse(getString(R.string.DawsonURL))
        startActivity(Intent(Intent.ACTION_VIEW, webpage))
    }

    /**
     * Function that will create an intent to go to the Settings activity.
     */
    private fun settingsClicked() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }


}