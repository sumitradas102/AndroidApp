package com.example.communitystay

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class complainactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complainactivity)
        // Get the current user's UID
        val uid = FirebaseAuth.getInstance().currentUser?.uid

// Check if UID is not null
        if (uid != null) {
            // Get DatabaseReference for the user's name
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("name")

            // Add ValueEventListener to retrieve the name
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val name = dataSnapshot.value as? String
                    if (name != null) {
                        // Name retrieved successfully
                        Log.d(TAG, "Name: $name")




                        findViewById<Button>(R.id.submit).setOnClickListener {
                            val complainText = findViewById<EditText>(R.id.complainedittext).text.toString()

                            // Check if complainer is not null
                           // val complainer = "Your complainer value here" // Make sure complainer is initialized
                            if (name != null) {
                                val mp = mapOf(
                                    "Complain text" to complainText,
                                    "Complainer" to name.toString()
                                )

// Get a reference to a new child node under "Complains" node using push()
                                val newComplainRef =
                                    FirebaseDatabase.getInstance().getReference("Complains").push()

// Set the value of the new child node
                                newComplainRef.setValue(mp)
                                    .addOnSuccessListener {
                                        // Data successfully written to the database
                                        Toast.makeText(
                                            baseContext,
                                            "Successfully uploaded complain",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        // Log.d(TAG, "Data successfully written to the database")
                                    }
                                    .addOnFailureListener { e ->
                                        // Error writing to the database
                                        Log.w(TAG, "Error writing to the database", e)
                                    }
                            }
                                else {
                                Log.e(TAG, "Complainer is null")
                            }
                        }










                        // Now you can use the 'name' variable
                    } else {
                        // Name is null
                        Log.d(TAG, "Name is null")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting name failed, log a message
                    Log.w(TAG, "loadName:onCancelled", databaseError.toException())
                }
            })
        } else {
            // UID is null
            Log.e(TAG, "Current user UID is null")
        }



    }
}