package com.example.communitystay

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.communitystay.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
import com.google.firebase.database.*

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var referenceNo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val roles = arrayOf("Manager", "Staff", "Resident")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        binding.spinner.setAdapter(adapter)

        binding.imgbtn.setOnClickListener {
            binding.spinner.showDropDown()
        }

        binding.buttonSignUp.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val pass = binding.password.text.toString().trim()
            val number = binding.phoneNumber.text.toString().trim()
            val status = binding.spinner.text.toString()

            if (name.isBlank() || email.isBlank() || pass.isBlank() || number.isBlank() || status.isBlank()) {
                Toast.makeText(this, "Fill all the information", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser!!.uid
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                        sendOtp(number, uid)
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

    private fun sendOtp(phoneNumber: String, uid: String) {
        val requestParameters = RequestParameters(
            appId = "APP_119062",
            password = "1b98469d4299b36d0128054acc23d5eb",
            mobile = phoneNumber
        )

        val destinationService = ServiceBuilder.buildService(MyApiService::class.java)
        val requestCall = destinationService.requestOtp(requestParameters)

        requestCall.enqueue(object:Callback<ApiResponse> {
            override fun onResponse(call:Call<ApiResponse>, response:Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    referenceNo = apiResponse?.referenceNo ?: ""
                    Toast.makeText(this@SignUp, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                    navigateToOtpActivity(uid)
                } else {
                    Toast.makeText(this@SignUp, "Failed to send OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@SignUp, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToOtpActivity(uid: String) {
        val intent = Intent(this,otp::class.java)
        intent.putExtra("REFERENCE_NO", referenceNo)
        intent.putExtra("UID", uid)
        startActivity(intent)
        finish()
    }
}
