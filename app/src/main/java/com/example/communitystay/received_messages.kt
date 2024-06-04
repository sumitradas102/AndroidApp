package com.example.communitystay

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class received_messages : AppCompatActivity() {
    var listofsender = ArrayList<String>()
    var senderName="rsgfdgsfddf"
    var senderUID=""
    var receiverUID=""
    var senderReceiverList = mutableListOf<Pair<String, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_messages)
        var myuid=FirebaseAuth.getInstance().uid



        // addLinearLayoutToScrollView("adsf")




        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val finalmap = ArrayList<Pair<String,Pair<String, Pair<String, String>>>>()
                    for (chatSnapshot in dataSnapshot.children) {
                        // This map will store time as key and message as value
                        val chatRoomId = chatSnapshot.key

                        var opponent=" "


                        if(chatSnapshot.child("user1").value.toString()==myuid.toString() ){
                            val messageMap = ArrayList<Pair<String, Pair<String, String>>>()
                            // addLinearLayoutToScrollView("adsf")
                            var opponentuid = chatSnapshot.child("user2").value.toString()
                            val opponentRef = FirebaseDatabase.getInstance().getReference("Users").child(opponentuid).child("name")

                            opponentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    opponent = dataSnapshot.value.toString()
                                    // Use opponentName as needed
                                    //  println("Opponent's Name: $opponentName")

                                    for(msgs in chatSnapshot.child("messages").children){
                                        var msgkey=msgs.key
                                        var sendmsg=chatSnapshot.child("messages").child(msgkey.toString()).child("sentmessage").value.toString()
                                        val thismsgtime = chatSnapshot.child("messages").child(msgkey.toString()).child("senttime").value.toString()
// Add the message and its time to the map
                                        // Convert Timestamp to a string representation
                                        //  val timestampString = thismsgtime?.toString() ?: ""

// Add the message and its time to the list as a Pair
                                        if(chatSnapshot.child("messages").child(msgkey.toString()).child("senderuid").value.toString()==opponentuid) {
                                            messageMap.add(
                                                Pair(
                                                    thismsgtime,
                                                    Pair(sendmsg, opponentuid)
                                                )
                                            )
                                        }
                                    }


                                    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

                                    messageMap.sortByDescending { it.first } // Assuming it.first is the timestamp as Long



                                    val lastEntry = messageMap.firstOrNull()

// Check if lastEntry is not null before using it
                                    var lasttime=""
                                    var lastMessage=""
                                    if (lastEntry != null) {
                                        lasttime = lastEntry.first
                                        lastMessage = lastEntry.second.first
                                        var thisopponentuid=lastEntry.second.second
                                        finalmap.add(Pair(lasttime, Pair(lastMessage,Pair(opponent,thisopponentuid) )))
                                        // addLinearLayoutToScrollView(lastMessage)
                                        // Do something with the last time and message
                                        // println("Last Time: $lastTime, Last Message: $lastMessage")
                                    } else {
                                        // Handle the case where there are no messages
                                        println("No messages found.")
                                        // addLinearLayoutToScrollView("no message")
                                    }


//            addLinearLayoutToScrollView("adsffgdfa")






                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle errors
                                    Toast.makeText(baseContext,"Error getting opponent's name: ${databaseError.message}",Toast.LENGTH_SHORT).show()
                                }
                            })
                            // addLinearLayoutToScrollView(chatSnapshot.child("user1").toString())
                        }

                        else if(chatSnapshot.child("user2").value.toString()==myuid.toString() ){
                            val messageMap = ArrayList<Pair<String, Pair<String, String>>>()
                            // addLinearLayoutToScrollView("adsf")
                            var opponentuid = chatSnapshot.child("user1").value.toString()
                            val opponentRef = FirebaseDatabase.getInstance().getReference("Users").child(opponentuid).child("name")

                            opponentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    opponent = dataSnapshot.value.toString()
                                    // Use opponentName as needed
                                    //  println("Opponent's Name: $opponentName")

                                    for(msgs in chatSnapshot.child("messages").children){
                                        var msgkey=msgs.key
                                        var sendmsg=chatSnapshot.child("messages").child(msgkey.toString()).child("sentmessage").value.toString()
                                        val thismsgtime = chatSnapshot.child("messages").child(msgkey.toString()).child("senttime").value.toString()
// Add the message and its time to the map
                                        // Convert Timestamp to a string representation
                                        //  val timestampString = thismsgtime?.toString() ?: ""

// Add the message and its time to the list as a Pair
                                        if(chatSnapshot.child("messages").child(msgkey.toString()).child("senderuid").value.toString()==opponentuid) {
                                            messageMap.add(
                                                Pair(
                                                    thismsgtime,
                                                    Pair(sendmsg, opponentuid)
                                                )
                                            )
                                        }
                                    }


                                    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

                                    messageMap.sortByDescending { it.first } // Assuming it.first is the timestamp as Long



                                    val lastEntry = messageMap.firstOrNull()

// Check if lastEntry is not null before using it
                                    var lasttime=""
                                    var lastMessage=""
                                    if (lastEntry != null) {
                                        lasttime = lastEntry.first
                                        lastMessage = lastEntry.second.first
                                        var thisopponentuid=lastEntry.second.second
                                        finalmap.add(Pair(lasttime, Pair(lastMessage,Pair(opponent,thisopponentuid) )))
                                        // addLinearLayoutToScrollView(lastMessage)
                                        // Do something with the last time and message
                                        // println("Last Time: $lastTime, Last Message: $lastMessage")
                                    } else {
                                        // Handle the case where there are no messages
                                        println("No messages found      fgsfada.")
                                        // addLinearLayoutToScrollView("no message")
                                    }


//            addLinearLayoutToScrollView("adsffgdfa")






                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle errors
                                    println("Error getting opponent's name: ${databaseError.message}")
                                }
                            })
                            // addLinearLayoutToScrollView(chatSnapshot.child("user1").toString())
                        }


                    }


                    val handler = Handler(Looper.getMainLooper())

// Delay execution by 2000 milliseconds (2 seconds)
                    handler.postDelayed({
                        //  val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

                        finalmap.sortByDescending { it.first } // Assuming it.first is the timestamp as Long
                        // addLinearLayoutToScrollView("adsffgdfa")
                        //addLinearLayoutToScrollView(finalmap.size.toString())
                        for ((time, msgpair) in finalmap) {
                            // Process each entry in reverse order
                            //println("Key: $key, Value: $value")

                            var latestmsg= msgpair.first
                            var latestopponent= msgpair.second.first
                            var latestopponentuid=msgpair.second.second
                            val formattedTime = convertMillisToDateFormat(time.toLong(), "dd-MM-yyyy HH:mm")
                            addLinearLayoutToScrollView(latestopponent +"  sent you a message "+formattedTime ,latestopponentuid)

                        }}, 10000)



// Traverse the list and call the method for each pair





                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })


// Assuming this is inside your activity or fragment



    }



    fun convertMillisToDateFormat(millis: Long, dateFormat: String): String {
        val date = Date(millis)
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        return sdf.format(date)
    }

    fun addLinearLayoutToScrollView(nameiwanttosendmsg: String,uidiwanttosendmsg:String) {
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
        textView.text = nameiwanttosendmsg
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
        newButton.text = "View messages"
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
            var gotochatpage=Intent(this,chatting_page::class.java)
            gotochatpage.putExtra("ownerUID",uidiwanttosendmsg)
            startActivity(gotochatpage)
            finish()
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

    private fun fetchSenderName(senderUID: String, callback: (String) -> Unit) {
        FirebaseDatabase.getInstance().getReference("Users")
            .child(senderUID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    if (userSnapshot.exists()) {
                        val senderName = userSnapshot.child("name").getValue(String::class.java).toString()
                        callback.invoke(senderName)
                    } else {
                        // Handle the case when user data doesn't exist
                        callback.invoke("Unknown Sender")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    callback.invoke("Unknown Sender")
                }
            })
    }


}