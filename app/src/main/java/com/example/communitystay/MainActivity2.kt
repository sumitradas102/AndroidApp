package com.example.communitystay

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val assignTaskButton = findViewById<Button>(R.id.Assigntask1)
        val menuButton = findViewById<Button>(R.id.menub)
        var uid=FirebaseAuth.getInstance().uid.toString()
        val statusRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("status")
        var complain=findViewById<Button>(R.id.complain)
        var f  = true
        statusRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val status = dataSnapshot.value // This will give you the value of status
                //Toast.makeText(baseContext,status,Toast.LENGTH_SHORT).show()
                if(status=="Staff" || status=="Manager"){
                    complain.text="View complains"
                    f = false
                }
                else complain.text="Complain"
                // Now you can use the status value
                Log.d(TAG, "Status is: $status")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting status failed, log a message
                Log.w(TAG, "loadStatus:onCancelled", databaseError.toException())
            }
        })



        // Set onClickListener for the Assign Task button
        assignTaskButton.setOnClickListener {
            // Create an intent to start MainActivity7
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
        }

        // Set onClickListener for the Menu button
        menuButton.setOnClickListener {
            // Create an intent to start MainActivity4
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.complain).setOnClickListener {
            // auth.signOut()
            if(f == false)
                startActivity(Intent(this, viewactivity::class.java))
            else startActivity(Intent(this, complainactivity::class.java))
            // finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        // If back button is pressed, close the app
        finishAffinity()
    }
}