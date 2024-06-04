package com.example.communitystay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class chatting_page : AppCompatActivity() {

    private lateinit var messagesReference: DatabaseReference
    var receiverUID =""
    private lateinit var textViewUserName: TextView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSendMessage: Button
    private lateinit var linearChat: LinearLayout
    // var ownername = " "
    var senderuid = " "
    var receiverName = " "
    var ownerUID = " "
    // var callonetime=false
    var senderName = " "
    private var fetchMessagesCalled = false // Flag to track if fetchPreviousMessages has been called
    //var chatRoomId="fsfdvdfs"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_page)


        // Initialize UI components
        textViewUserName = findViewById(R.id.textViewUserName)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSendMessage = findViewById(R.id.buttonSendMessage)
        linearChat = findViewById(R.id.linearChat)

        // Initialize Firebase references
        ownerUID = intent.getStringExtra("ownerUID").toString()

        senderuid = FirebaseAuth.getInstance().uid.toString()


        receiverUID = ownerUID





        val database = FirebaseDatabase.getInstance()
        messagesReference = database.getReference("chats")
        // Inside your onCreate method or wherever appropriate

        val receiverNameRef = FirebaseDatabase.getInstance().getReference("Users").child(receiverUID).child("name")
        receiverNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    receiverName = dataSnapshot.value.toString()
                    textViewUserName.text = receiverName  // Set the name in your UI
                } else {
                    // Handle the case when data doesn't exist
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })

        val senderNameRef = FirebaseDatabase.getInstance().getReference("Users").child(senderuid).child("name")
        senderNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    senderName = dataSnapshot.value.toString()
                } else {
                    // Handle the case when data doesn't exist
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })

        messagesReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (chatSnapshot in snapshot.children) {

                    if(chatSnapshot.child("user1").value.toString()==senderuid && chatSnapshot.child("user2").value.toString()==ownerUID){
                        var msgs=chatSnapshot.child("messages")
                        for(singlemsg in msgs.children){
                            displayMessage(singlemsg.child("sentmessage").value.toString())
                        }
                        break



                    }

                    if(chatSnapshot.child("user2").value.toString()==senderuid && chatSnapshot.child("user1").value.toString()==ownerUID){
                        var msgs=chatSnapshot.child("messages")
                        for(singlemsg in msgs.children){
                            displayMessage(singlemsg.child("sentmessage").value.toString())
                        }
                        break



                    }


                }
            }


            // Other methods for handling changes, removals, and movements

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })


        // Set up UI
        textViewUserName.text = receiverName


        // Set up click listener for the "Send Message" button
        buttonSendMessage.setOnClickListener {
            fetchMessagesCalled = true
            sendMessage()
        }

        // Listen for new messages

    }
    private fun sendMessage() {
        val messageText = editTextMessage.text.toString()
        if (messageText.isNotEmpty()) {
            val senderUserId = FirebaseAuth.getInstance().currentUser?.uid

            FirebaseDatabase.getInstance().getReference("chats").addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    var x=""
                    for (chatSnapshot in snapshot.children) {

                        if(chatSnapshot.child("user1").value.toString()==senderuid && chatSnapshot.child("user2").value.toString()==receiverUID){
                            var msgs=chatSnapshot.child("messages")
                            // for(singlemsg in msgs.children){
                            //  displayMessage(singlemsg.toString())
                            //}
                            // var x="asasdf"
                            x=chatSnapshot.key.toString()
                            //    var allmsgs= mapOf(
                            //  "messages" to messageText
                            //)

                            break


                        }

                        else if(chatSnapshot.child("user2").value.toString()==senderuid && chatSnapshot.child("user1").value.toString()==ownerUID){

                            x=chatSnapshot.key.toString()

                            break


                        }

                    }

                    // val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                    //val formattedDateTime = LocalDateTime.now().format(dateTimeFormatter)  // Replace with your actual LocalDateTime

                    var bal = messagesReference.child(x).child("messages").push().key
                    messagesReference.child(x).child("messages").child(bal!!).child("sentmessage").setValue("$senderName : $messageText")
                    messagesReference.child(x).child("messages").child(bal!!).child("senderuid").setValue(senderUserId)
                    messagesReference.child(x).child("messages").child(bal!!).child("senttime").setValue(System.currentTimeMillis())
                    messagesReference.child(x).child("messages").child(bal!!).child("Notification_status").setValue("unnotified")
                    displayMessage("$senderName : $messageText")

                }


                // Other methods for handling changes, removals, and movements

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })





            editTextMessage.text.clear()
        }
    }

    private fun displayMessage(message:String) {
        // Create a TextView for the new message
        val messageTextView = TextView(this)
        messageTextView.setTextColor(ContextCompat.getColor(this, R.color.black))
        messageTextView.text = message
        messageTextView.textSize =30f
        linearChat.addView(messageTextView)

        // Scroll the ScrollView to the bottom to show the latest message
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    // Inside a class or method that is not in an Activity or Fragment


    fun generateChatRoomId(user1Id: String, user2Id: String): String {
        val sortedUserIds = listOf(user1Id, user2Id).sorted().joinToString("_")
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val randomChars = (1..4).map { ('A'..'Z').random() }.joinToString("")
        return "CHATROOM_$sortedUserIds$timestamp$randomChars"
    }



    override fun onBackPressed() {
        var back=Intent(this,MainActivity6::class.java);
        back.putExtra("staffuid",receiverUID)
        super.onBackPressed()
    }
}