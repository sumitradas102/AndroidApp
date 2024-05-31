
package com.example.communitystay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.communitystay.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date

class SignUp : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var number: String
    private lateinit var pass: String
    private lateinit var status: String
    private val PICK_IMAGE_REQUEST = 2

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val lst: Array<String> = arrayOf("Manager", "Staff", "Resident")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, lst)
        binding.spinner.setAdapter(adapter)

        binding.imgbtn.setOnClickListener {
            binding.spinner.showDropDown()
        }

        binding.buttonSignUp.setOnClickListener {
            name = binding.name.text.toString().trim()
            email = binding.email.text.toString().trim()
            pass = binding.password.text.toString().trim()
            number = binding.phoneNumber.text.toString().trim()
            status = binding.spinner.text.toString()

            if (name.isBlank() || email.isBlank() || pass.isBlank() || number.isBlank() || status.isBlank()) {
                Toast.makeText(this, "Fill all the information", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var uid=FirebaseAuth.getInstance().uid.toString()
                        Toast.makeText(this, "Created and user account", Toast.LENGTH_SHORT).show()
                        saveInfo(name, email, name, number, status,uid)
                        startActivity(Intent(this, MainActivity2::class.java))
                    } else {
                        val errorMessage = task.exception?.message.toString()
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.goLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    private fun saveInfo(
        restaurantName: String,
        ownerName: String,
        email: String,
        password: String,
        status: String,
        uid:String
    ) {
        val dataModelSaver = UserData(restaurantName, ownerName, email, status,uid)
        val userId = auth.currentUser!!.uid

        databaseReference.child("Users").child(userId).setValue(dataModelSaver)
            .addOnSuccessListener {

                // Iterate through all registered users
                FirebaseDatabase.getInstance().getReference("Users")
                    .addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val numberOfUsers = snapshot.childrenCount.toInt()
                            if(numberOfUsers>=2) {
                                for (usersuids in snapshot.children) {

                                    if(userId!=usersuids.key){
                                        var theirchatroom= generateChatRoomId(userId,usersuids.key.toString())
                                        var couple= mapOf(
                                            "user1" to uid,
                                            "user2" to usersuids.key.toString(),
                                            "messages" to null
                                        )
                                        FirebaseDatabase.getInstance().getReference("chats").child(theirchatroom).setValue(couple)


                                    }

                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })




                Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
                Log.d("uploaddata", "upload success")

                if (status == "Staff") {
                    addStaffMember(ownerName, email, number, userId)
                }
                else if (status == "Resident") {
                    addResident(ownerName, email, number,userId)
                }
                else if (status == "Manager") {
                    addManager(ownerName, email, number,userId)
                }
            }
            .addOnFailureListener { e ->
                Log.d("uploaddata", e.message.toString())
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun addStaffMember(email: String, name: String, number: String, userID:String) {
        val staffData = mapOf(
            "name" to name,
            "email" to email,
            "number" to number,
            "usedID" to userID
        )

        databaseReference.child("Staff").push().setValue(staffData)
            .addOnSuccessListener {
                Toast.makeText(this, "Staff member added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("uploaddata", e.message.toString())
                Toast.makeText(this, "Failed to add staff member", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addResident(email: String, name: String, number: String,userID:String) {
        val residentData = mapOf(
            "name" to name,
            "email" to email,
            "number" to number,
            "usedID" to userID

        )

        databaseReference.child("Resident").push().setValue(residentData)
            .addOnSuccessListener {
                Toast.makeText(this, "Resident added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("uploaddata", e.message.toString())
                Toast.makeText(this, "Failed to add Resident", Toast.LENGTH_SHORT).show()
            }
    }
    private fun addManager(email: String, name: String, number: String, userID :String) {
        val ManagerData = mapOf(
            "name" to name,
            "email" to email,
            "number" to number,
            "usedID" to userID
        )

        databaseReference.child("Manager").push().setValue(ManagerData)
            .addOnSuccessListener {
                Toast.makeText(this, "Manager added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("uploaddata", e.message.toString())
                Toast.makeText(this, "Failed to add Manager", Toast.LENGTH_SHORT).show()
            }
    }



    fun generateChatRoomId(user1Id: String, user2Id: String): String {
        val sortedUserIds = listOf(user1Id, user2Id).sorted().joinToString("_")
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val randomChars = (1..4).map { ('A'..'Z').random() }.joinToString("")
        return "CHATROOM_$sortedUserIds$timestamp$randomChars"
        }
}


/*
package com.example.communitystay

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.communitystay.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date

class SignUp : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var number: String
    private lateinit var pass: String
    private lateinit var status: String
    private val PICK_IMAGE_REQUEST = 2
    private var imageUri: Uri? = null

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val lst: Array<String> = arrayOf("Manager", "Staff", "Resident")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, lst)
        binding.spinner.setAdapter(adapter)

        binding.imgbtn.setOnClickListener {
            binding.spinner.showDropDown()
        }

        // Set OnClickListener for selecting an image
        binding.imageViewBackground.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        binding.buttonSignUp.setOnClickListener {
            name = binding.name.text.toString().trim()
            email = binding.email.text.toString().trim()
            pass = binding.password.text.toString().trim()
            number = binding.phoneNumber.text.toString().trim()
            status = binding.spinner.text.toString()

            if (name.isBlank() || email.isBlank() || pass.isBlank() || number.isBlank() || status.isBlank()) {
                Toast.makeText(this, "Fill all the information", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var uid=FirebaseAuth.getInstance().uid.toString()
                        Toast.makeText(this, "Created and user account", Toast.LENGTH_SHORT).show()
                        saveInfo(name, email, pass, number, status,uid)
                        startActivity(Intent(this, MainActivity2::class.java))
                    } else {
                        val errorMessage = task.exception?.message.toString()
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.goLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        // Load image URI if available
        val imageUriString = sharedPreferences.getString("imageUri", null)
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString)
            binding.imageViewBackground.setImageURI(imageUri)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.imageViewBackground.setImageURI(imageUri)

            // Save image URI in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("imageUri", imageUri.toString())
            editor.apply()
        }
    }

    private fun saveInfo(
        restaurantName: String,
        ownerName: String,
        email: String,
        password: String,
        status: String,
        uid:String
    ) {
        val dataModelSaver = UserData(restaurantName, ownerName, email, status,uid)
        val userId = auth.currentUser!!.uid

        databaseReference.child("Users").child(userId).setValue(dataModelSaver)
            .addOnSuccessListener {

                // Iterate through all registered users
                FirebaseDatabase.getInstance().getReference("Users")
                    .addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val numberOfUsers = snapshot.childrenCount.toInt()
                            if(numberOfUsers>=2) {
                                for (usersuids in snapshot.children) {

                                    if(userId!=usersuids.key){
                                        var theirchatroom= generateChatRoomId(userId,usersuids.key.toString())
                                        var couple= mapOf(
                                            "user1" to uid,
                                            "user2" to usersuids.key.toString(),
                                            "messages" to null
                                        )
                                        FirebaseDatabase.getInstance().getReference("chats").child(theirchatroom).setValue(couple)


                                    }

                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })




                Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
                Log.d("uploaddata", "upload success")

                if (status == "Staff") {
                    addStaffMember(ownerName, email, number, userId)
                }
                else if (status == "Resident") {
                    addResident(ownerName, email, number,userId)
                }
                else if (status == "Manager") {
                    addManager(ownerName, email, number,userId)
                }
            }
            .addOnFailureListener { e ->
                Log.d("uploaddata", e.message.toString())
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun addStaffMember(email: String, name: String, number: String, userID:String) {
        val staffData = mapOf(
            "name" to name,
            "email" to email,
            "number" to number,
            "usedID" to userID
        )

        databaseReference.child("Staff").push().setValue(staffData)
            .addOnSuccessListener {
                Toast.makeText(this, "Staff member added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("uploaddata", e.message.toString())
                Toast.makeText(this, "Failed to add staff member", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addResident(email: String, name: String, number: String,userID:String) {
        val residentData = mapOf(
            "name" to name,
            "email" to email,
            "number" to number,
            "usedID" to userID

        )

        databaseReference.child("Resident").push().setValue(residentData)
            .addOnSuccessListener {
                Toast.makeText(this, "Resident added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("uploaddata", e.message.toString())
                Toast.makeText(this, "Failed to add Resident", Toast.LENGTH_SHORT).show()
            }
    }
    private fun addManager(email: String, name: String, number: String, userID :String) {
        val ManagerData = mapOf(
            "name" to name,
            "email" to email,
            "number" to number,
            "usedID" to userID
        )

        databaseReference.child("Manager").push().setValue(ManagerData)
            .addOnSuccessListener {
                Toast.makeText(this, "Manager added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("uploaddata", e.message.toString())
                Toast.makeText(this, "Failed to add Manager", Toast.LENGTH_SHORT).show()
            }
    }



    fun generateChatRoomId(user1Id: String, user2Id: String): String {
        val sortedUserIds = listOf(user1Id, user2Id).sorted().joinToString("_")
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val randomChars = (1..4).map { ('A'..'Z').random() }.joinToString("")
        return "CHATROOM_$sortedUserIds$timestamp$randomChars"
    }
}
*/