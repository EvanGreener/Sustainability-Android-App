package com.sustaincsej.sustain_cedricsebevanjean.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*
import com.sustaincsej.sustain_cedricsebevanjean.R
import com.sustaincsej.sustain_cedricsebevanjean.models.Fact

class SustainabilityFactsActivity : AppCompatActivity() {

    private lateinit var fire: FirebaseDatabase
    private lateinit var database: DatabaseReference

    val factsEventListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            Log.d("TEST", "im here")
            val thing = dataSnapshot.children.iterator()
            if (thing.hasNext()) {
                val index = thing.next()
                val itemIterator = index.children.iterator()

                while (itemIterator.hasNext()) {
                    val current = itemIterator.next()
                    val map = current.value as HashMap<*, *>
                    val tip = Fact(
                        current.key!!.toString(),
                        map["image_url"] as String?,
                        map["source_url"] as String?,
                        map["text"] as String?
                    )
                    Log.i("TEST", tip.text)
                }
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

        fire = FirebaseDatabase.getInstance()
        Log.d("TEST", fire.app.toString())
        Log.d("TEST", FirebaseDatabase.getSdkVersion())
        database = FirebaseDatabase.getInstance().reference.child("facts")
        Log.d("TEST", database.key)


        database.addValueEventListener(factsEventListener)
    }

    private fun getFacts() {

    }
}
