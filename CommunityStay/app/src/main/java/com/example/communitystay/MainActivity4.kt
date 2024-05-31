/*package com.example.communitystay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainActivity4 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var nameTextView: TextView
    private lateinit var statusTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val staffButton = findViewById<Button>(R.id.Staff)

        staffButton.setOnClickListener {
                // Create an intent to start MainActivity4
                val intent = Intent(this, MainActivity5::class.java)
                startActivity(intent)
            }



        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let { user ->
            val userEmail: String = user.email ?: ""
            val userQuery: Query = databaseReference.child("Users").orderByChild("email").equalTo(userEmail)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userData: UserData? = userSnapshot.getValue(UserData::class.java)
                            userData?.let {
                                // Set name and status TextViews

                                nameTextView.text = userData.name
                                statusTextView.text = userData.status
                            }
                        }
                    } else {
                        // User data not found
                        // Handle this case, e.g., show a message or log an error
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    // Log error message
                    error.message?.let {
                        println("Database Error: $it")
                    }
                }
            })
        }
    }

}
*/
/*
package com.example.communitystay

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainActivity4 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var nameTextView: TextView
    private lateinit var statusTextView: TextView
    val currentUser = FirebaseAuth.getInstance().currentUser
    var uid=FirebaseAuth.getInstance().uid.toString()
    val statusRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("status")
    //val statusRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("status")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        nameTextView = findViewById(R.id.textViewTitle)
        statusTextView = findViewById(R.id.status)

        val staffButton = findViewById<Button>(R.id.Staff)

        staffButton.setOnClickListener {
            // Create an intent to start MainActivity5
            val intent = Intent(this, MainActivity5::class.java)
            startActivity(intent)
        }


        val managerButton = findViewById<Button>(R.id.Manager)

        managerButton.setOnClickListener {
            // Create an intent to start MainActivity5
            val intent = Intent(this, MainActivity9::class.java)
            startActivity(intent)
        }

        val residentButton = findViewById<Button>(R.id.logoutButton)

        residentButton.setOnClickListener {
            // Create an intent to start MainActivity5
            val intent = Intent(this, MainActivity8::class.java)
            startActivity(intent)
        }

        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let { user ->
            val userEmail: String = user.email ?: ""
            val userQuery: Query = databaseReference.child("Users").orderByChild("email").equalTo(userEmail)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userData: UserData? = userSnapshot.getValue(UserData::class.java)
                            userData?.let {
                                // Set name and status TextViews
                                nameTextView.text = userData.name
                                statusTextView.text = userData.status
                            }
                        }
                    } else {
                        // User data not found
                        // Handle this case, e.g., show a message or log an error
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    // Log error message
                    error.message?.let {
                        println("Database Error: $it")
                    }
                }
            })
        }
    }
}
*/
package com.example.communitystay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity4 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var nameTextView: TextView
    private lateinit var statusTextView: TextView
    var f = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        nameTextView = findViewById(R.id.textViewTitle)
        statusTextView = findViewById(R.id.status)

        val staffButton = findViewById<Button>(R.id.Staff)
        val managerButton = findViewById<Button>(R.id.Manager)
        val residentButton = findViewById<Button>(R.id.logoutButton)

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userEmail: String = user.email ?: ""
            val userQuery: Query = databaseReference.child("Users").orderByChild("email").equalTo(userEmail)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userData: UserData? = userSnapshot.getValue(UserData::class.java)
                            userData?.let {
                                nameTextView.text = userData.name
                                statusTextView.text = userData.status
                                if (userData.status == "Resident") {
                                    f = false
                                    residentButton.visibility = View.GONE
                                }
                            }
                        }
                    } else {
                        // User data not found
                        // Handle this case, e.g., show a message or log an error
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    // Log error message
                    error.message?.let {
                        println("Database Error: $it")
                    }
                }
            })
        }

        staffButton.setOnClickListener {
            val intent = Intent(this, MainActivity5::class.java)
            startActivity(intent)
        }

        managerButton.setOnClickListener {
            val intent = Intent(this, MainActivity9::class.java)
            startActivity(intent)
        }
        //if (f == true)
        residentButton.setOnClickListener {
            val intent = Intent(this, MainActivity8::class.java)
            startActivity(intent)
        }


    }
}
