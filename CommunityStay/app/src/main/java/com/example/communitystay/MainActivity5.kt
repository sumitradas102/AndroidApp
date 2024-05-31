package com.example.communitystay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity5 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var staffLayout: LinearLayout

    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentuid = currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        staffLayout = findViewById(R.id.staffLayout)

        // Read staff members from Firebase Realtime Database
        readStaffMembers()
    }

    private fun readStaffMembers() {
        val staffListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                staffLayout.removeAllViews() // Clear existing views

                for (childSnapshot in dataSnapshot.children) {

                    val staffName = childSnapshot.child("name").getValue(String::class.java)
                    val staffEmail = childSnapshot.child("email").getValue(String::class.java)
                    val staffPhone = childSnapshot.child("number").getValue(String::class.java)
                    var uid=childSnapshot.child("usedID").getValue(String::class.java)
                    if(currentuid != uid)
                    staffName?.let {
                        addButtonToLayout(it, staffEmail.orEmpty(), staffPhone.orEmpty(),uid.toString())
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("ReadStaffError", "Failed to read staff members: ${databaseError.message}")
                Toast.makeText(this@MainActivity5, "Failed to read staff members", Toast.LENGTH_SHORT).show()
            }
        }

        // Attach ValueEventListener to "Staff" node
        databaseReference.child("Staff").addListenerForSingleValueEvent(staffListener)
    }

    private fun addButtonToLayout(staffName: String, staffEmail: String, staffPhone: String,staffuid:String) {
        val button = Button(this)
        button.text = staffName
        button.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        staffLayout.addView(button)

        // Set OnClickListener for the button
        button.setOnClickListener {
            // Start MainActivity6 with staff data as extras
            val intent = Intent(this@MainActivity5, MainActivity6::class.java)
            intent.putExtra("staffName", staffName)
            intent.putExtra("staffEmail", staffEmail)
            intent.putExtra("staffPhone", staffPhone)
            intent.putExtra("staffuid",staffuid)

            //Toast.makeText(this,staffuid,Toast.LENGTH_SHORT).show()

            startActivity(intent)
        }
    }
}
