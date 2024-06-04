package com.example.communitystay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Check if user is signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, redirect to main activity
            startActivity(Intent(this, MainActivity2::class.java))
            finish() // Finish the current activity to prevent going back to MainActivity
        }

        val startButton = findViewById<Button>(R.id.start)
        startButton.setOnClickListener {
            // Start the login activity
            startActivity(Intent(this@MainActivity, Login::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // If back button is pressed, close the app
        finishAffinity()
    }
}
