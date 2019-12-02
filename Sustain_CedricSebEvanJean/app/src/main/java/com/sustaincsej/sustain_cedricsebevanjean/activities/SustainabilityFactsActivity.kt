package com.sustaincsej.sustain_cedricsebevanjean.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.models.Fact
import java.util.*
import kotlin.collections.ArrayList

class SustainabilityFactsActivity : AppCompatActivity() {

    private lateinit var factImage: ImageButton
    private lateinit var factText: TextView
    private lateinit var factSource: TextView

    private lateinit var fire: FirebaseDatabase
    private lateinit var database: DatabaseReference

    private val facts: ArrayList<Fact> = ArrayList()

    private val random: Random = Random()
    private var factsIndex: Int = 0

    private var loaded = false

    private val factsEventListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()) {
                val allFacts = dataSnapshot.children.iterator()

                //For every element
                while(allFacts.hasNext()){
                    val currentFact = allFacts.next()
                    val currentFactHashMap = currentFact.value as HashMap<*, *>

                    val fact = Fact(
                        currentFactHashMap["image_url"].toString(),
                                    currentFactHashMap["source_url"].toString(),
                                    currentFactHashMap["text"].toString())

                    facts.add(fact)
                }

                //Dislpay random fact to start if needed
                if (!loaded) {
                    randomFact()
                    loaded=true
                } else {
                    showFact(factsIndex)
                }
            }
            else {
                Log.i(TAG, "nope")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "Failed to read", error.toException())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sustainability_facts)

        //Get views
        factImage = findViewById(R.id.sust_fact_image)
        factText = findViewById(R.id.sust_fact_text)
        factSource = findViewById(R.id.sust_fact_source)
    }

    override fun onResume() {
        super.onResume()
        //Create database connection and information
        fire = FirebaseDatabase.getInstance()
        Log.d(TAG, fire.app.toString())
        database = fire.reference.child("facts")
        database.addListenerForSingleValueEvent(factsEventListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putInt("index", factsIndex)
            putBoolean("loaded", loaded)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        loaded = savedInstanceState.getBoolean("loaded", false)
        factsIndex = savedInstanceState.getInt("index", 0)
    }

    /**
     * Function that is called when the user clicks on the fact image
     *
     * @param v The View/image
     */
    fun handleFactImageClick(v: View) {
        nextFact()
    }

    /**
     * Goes to the link when clicked
     */
    fun handleLinkClick(v: View) {
        val webpage: Uri = Uri.parse(factSource.text.toString())
        startActivity(Intent(Intent.ACTION_VIEW, webpage))
    }

    /**
     * Function that gets a random fact from the firebase database
     */
    private fun randomFact() {
        factsIndex = random.nextInt(facts.size)
        showFact(factsIndex)
    }

    /**
     * Function that gets the next fact from firebase.
     *
     * Increments to factsIndex by one until it reaches the max size, then resets to zero and restarts.
     */
    private fun nextFact() {
        factsIndex++
        if (factsIndex>=facts.size) {
            factsIndex=0
        }
        showFact(factsIndex)
    }

    private fun showFact(factsIndex: Int) {
        val fact = facts[factsIndex]
        factText.text = fact.text
        factSource.text = fact.source_url
        Picasso.get().load(fact.image_url).into(factImage)
    }

    companion object {
        private val TAG = "SustFactActivity"
    }

}
