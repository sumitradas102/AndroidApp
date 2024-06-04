
package com.example.communitystay
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity3 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var nameTextView: TextView
    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        nameTextView = findViewById(R.id.textViewTitle)
        statusTextView = findViewById(R.id.status)

        val assignTaskButton = findViewById<Button>(R.id.assignTaskButton)
        val notificationButton = findViewById<Button>(R.id.notificationButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        assignTaskButton.setOnClickListener {
            startActivity(Intent(this, MainActivity4::class.java))
        }

        notificationButton.setOnClickListener {
            startActivity(Intent(this, received_messages::class.java))
        }



        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }


        // Fetch and display user data
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
