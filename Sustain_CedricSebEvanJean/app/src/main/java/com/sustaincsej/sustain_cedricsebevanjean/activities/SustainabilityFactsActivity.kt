package com.sustaincsej.sustain_cedricsebevanjean.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.*
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

    private val factsEventListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()) {
                val post = dataSnapshot.getValue(Fact::class.java)
                facts.add(post!!)
                Log.i("TEST", post?.text)
            }
            else {
                Log.i("TEST", "nope")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("TEST", "Failed to read", error.toException())
            println("Hello")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sustainability_facts)

        //Get views
        factImage = findViewById(R.id.sust_fact_image)
        factText = findViewById(R.id.sust_fact_text)
        factSource = findViewById(R.id.sust_fact_source)

        //Create database connection and information
        fire = FirebaseDatabase.getInstance()
        Log.d("TEST", fire.app.toString())
        Log.d("TEST", FirebaseDatabase.getSdkVersion())
        database = fire.reference.child("facts")
        Log.d("TEST", database.key)
        database.addValueEventListener(factsEventListener)

        //Display random fact to start
        randomFact()
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
     * Function that gets a random fact from the firebase database
     */
    private fun randomFact() {
        factsIndex = random.nextInt(6)
        Log.i("TEST", factsIndex.toString())
    }

    /**
     * Function that gets the next fact from firebase.
     *
     * Increments to factsIndex by one until it reaches the max size, then resets to zero and restarts.
     */
    private fun nextFact() {
        if (factsIndex == facts.size) {
            factsIndex = 0
        }
        else {
            factsIndex++
        }
        Log.i("TEST", factsIndex.toString())
    }

}
