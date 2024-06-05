package com.example.communitystay
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.communitystay.MyApiService
import com.example.communitystay.ServiceBuilder
import com.example.communitystay.ApiResponse
import com.example.communitystay.RequestParameters
import com.google.firebase.auth.FirebaseAuth
import android.widget.ArrayAdapter
import com.example.communitystay.databinding.ActivitySignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
class otp : AppCompatActivity() {

    private lateinit var otpEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var referenceNo: String

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var number: String
    private lateinit var pass: String
    private lateinit var status: String

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        otpEditText = findViewById(R.id.otp)
        submitButton = findViewById(R.id.buttonotp)

        referenceNo = intent.getStringExtra("REFERENCE_NO") ?: ""
        name = intent.getStringExtra("NAME") ?: ""
        email = intent.getStringExtra("EMAIL") ?: ""
        number = intent.getStringExtra("NUMBER") ?: ""
        pass = intent.getStringExtra("PASS") ?: ""
        status = intent.getStringExtra("STATUS") ?: ""

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        submitButton.setOnClickListener {
            val otp = otpEditText.text.toString().trim()
            if (otp.isNotEmpty()) {
                verifyOtp(otp)
            } else {
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyOtp(otp: String) {
        val verifyParameters = VerifyParameters(
            appId = "APP_118923",
            password = "1b98469d4299b36d0128054acc23d5eb",
            referenceNo = referenceNo,
            otp = otp
        )

        val destinationService = ServiceBuilder.buildService(MyApiService::class.java)
        val requestCall = destinationService.verifyOtp(verifyParameters)

        requestCall.enqueue(object : Callback<OtpVerifyRespone> {
            override fun onResponse(call: Call<OtpVerifyRespone>, response: Response<OtpVerifyRespone>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.status == "SUCCESS") {
                        Toast.makeText(this@otp, "OTP verified successfully", Toast.LENGTH_SHORT).show()
                        saveInfo(name, email, pass, number, status, FirebaseAuth.getInstance().uid ?: "")
                    } else {
                        // If OTP verification fails, delete the UID from the database
                        deleteUidFromDatabase()
                        Toast.makeText(this@otp, "OTP verification failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@otp, "Failed to verify OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OtpVerifyRespone>, t: Throwable) {
                Toast.makeText(this@otp, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
                    .addListenerForSingleValueEvent(object : ValueEventListener {
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

    private fun deleteUidFromDatabase() {
        val userId = auth.currentUser!!.uid
        databaseReference.child("Users").child(userId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "User data deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to delete user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun navigateToMainActivity2() {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }
}
