package com.example.communitystay

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class viewactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewactivity)
        FirebaseDatabase.getInstance().getReference("Complains")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (complainSnapshot in dataSnapshot.children) {
                        val complainText = complainSnapshot.child("Complain text").value.toString()
                        val complainer = complainSnapshot.child("Complainer").value.toString()
                        // Here, you can use complainText and complainer as per your requirement
                        // For example, you can display them in a TextView or add them to a list
                        addLinearLayoutToScrollView(complainer, complainText)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
    }



        fun addLinearLayoutToScrollView(name: String,complain:String) {
        // Check if the sender's UID is not equal to your UID and the receiver's UID is equal to your UID

        // Create a new LinearLayout
        val newLinearLayout = LinearLayout(this)
        newLinearLayout.orientation = LinearLayout.VERTICAL
        newLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Create a horizontal LinearLayout for the content
        val contentLayout = LinearLayout(this)
        contentLayout.orientation = LinearLayout.HORIZONTAL
        contentLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Fetch sender's name asynchronously

        // Create a new TextView for sender's name
        val textView = TextView(this)
        textView.text = name+"'s complain"
        textView.textSize = 24f // Adjust the text size
        textView.layoutParams = LinearLayout.LayoutParams(
            0, // Set width to 0 to allow weight to work
            LinearLayout.LayoutParams.WRAP_CONTENT,

            1f // Set weight to 1 to make it take remaining space
        )

        // Add the TextView to the content LinearLayout
        contentLayout.addView(textView)

        // Create a new Button for view messages
        val newButton = Button(this)
        newButton.text = "View complain"
        val buttonLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            100 // Set a fixed height (adjust as needed)
        )
        buttonLayoutParams.gravity =
            Gravity.START or Gravity.CENTER_VERTICAL // Align the button to the left
        newButton.layoutParams = buttonLayoutParams
        newButton.textSize = 12f // Adjust the text size
        newButton.setPadding(8, 0, 8, 0) // Adjust padding if needed

        // Add the Button to the content LinearLayout
        contentLayout.addView(newButton)

        contentLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))

        newButton.setOnClickListener(){
          //  var gotochatpage= Intent(this,chatting_page::class.java)
            //gotochatpage.putExtra("ownerUID",uidiwanttosendmsg)
           // startActivity(gotochatpage)
            //finish()

            var onepage=Intent(this,viewinonepage::class.java)
            onepage.putExtra("Complain text",complain)
            startActivity(onepage)


        }










        // Add the content LinearLayout to the new LinearLayout
        newLinearLayout.addView(contentLayout)

        // Add a line view at the top
        val topLine = View(this)
        topLine.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            1 // Height of the line (adjust as needed)
        )
        topLine.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))

        // Add the top line to the new LinearLayout
        newLinearLayout.addView(topLine)

        // Add the new LinearLayout to the existing parent LinearLayout inside ScrollView
        val parentLinearLayout: LinearLayout = findViewById(R.id.parentLinearLayout)
        parentLinearLayout.addView(newLinearLayout)

        // Add a line view at the bottom
        val bottomLine = View(this)
        bottomLine.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            1 // Height of the line (adjust as needed)
        )
        bottomLine.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))

        // Add the bottom line to the parent LinearLayout
        parentLinearLayout.addView(bottomLine)

    }
}