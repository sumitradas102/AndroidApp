
package com.example.communitystay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class MainActivity6 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        // Retrieve staff data from intent extras
        val staffName = intent.getStringExtra("staffName")
        val staffEmail = intent.getStringExtra("staffEmail")
        val staffPhone = intent.getStringExtra("staffPhone")
        var uid=intent.getStringExtra("staffuid").toString()
        // Find the TextViews by id
        val nameTextView = findViewById<TextView>(R.id.name)
        val emailTextView = findViewById<TextView>(R.id.mail)
        val phoneTextView = findViewById<TextView>(R.id.Phn)

        // Set the staff data to the TextViews
        nameTextView.text = staffName
        emailTextView.text = staffEmail
        phoneTextView.text = staffPhone

        // Find the button by id for calling
        val callButton = findViewById<Button>(R.id.b1)

        // Set OnClickListener for the call button
        callButton.setOnClickListener {
            // Create an intent to initiate a phone call
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$staffPhone")

            // Check if there's an app to handle the intent
            if (callIntent.resolveActivity(packageManager) != null) {
                // Start the intent
                startActivity(callIntent)
            } else {
                // Handle the case where there's no app to handle the intent
                // For example, display a toast message
                Toast.makeText(this, "No app to handle phone calls", Toast.LENGTH_SHORT).show()
            }
        }

        // Find the button by id for sending email
        val emailButton = findViewById<Button>(R.id.b2)

        // Set OnClickListener for the email button
        emailButton.setOnClickListener {
            // Create an intent to send an email via Gmail
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:$staffEmail") // Specify recipient's Gmail address
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject") // Specify the email subject
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body") // Specify the email body

            // Check if there's an app to handle the intent

            // Start the intent
            startActivity(emailIntent)

        }

        val messagebutton = findViewById<Button>(R.id.b3)
        messagebutton.setOnClickListener {
            val intent = Intent(this, chatting_page::class.java)

            //Toast.makeText(this,uid,Toast.LENGTH_SHORT).show()
            intent.putExtra("ownerUID",uid)
            // intent.putExtra()
            startActivity(intent)
        }
    }
}
