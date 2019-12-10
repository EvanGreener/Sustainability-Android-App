package com.sustaincsej.sustain_cedricsebevanjean.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sustaincsej.sustain_cedricsebevanjean.R
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

/**
 * Class that displays a small paragraph about the app itself and about the programmers of it.
 *
 * @author Jean Robatto
 * @author Sebastien Palin
 */
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    /**
     * When a developer image is clicked, show an alert with a personalized message
     */
    fun handleImageViewClick(view: View) {
        when (view.id) {
            R.id.about_cedric_pic -> showDialog(resources.getString(R.string.about_cedric_name), resources.getString(R.string.about_cedric_message))
            R.id.about_jean_pic -> showDialog(resources.getString(R.string.about_jean_name), resources.getString(R.string.about_jean_message))
            R.id.about_evan_pic -> showDialog(resources.getString(R.string.about_evan_name), resources.getString(R.string.about_evan_message))
            R.id.about_seb_pic -> showDialog(resources.getString(R.string.about_seb_name), resources.getString(R.string.about_seb_message))
            else -> return
        }
    }

    /**
     * Pops up a dialog with the developer's name as title
     * and a short message about them.
     */
    private fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setTitle(title)
            .setCancelable(false)
            .setPositiveButton("COOL!", DialogInterface.OnClickListener { dialog, id ->
                //do nothing (required for constructor)
            })
        val alert = builder.create()
        alert.show()
    }
}
