package com.example.communitystay
import com.example.communitystay.data.ApiResponse
import com.example.communitystay.data.RequestParameters
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.communitystay.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var number: String
    private lateinit var pass: String
    private lateinit var status: String

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
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
                Log.d("SignUp", "Attempting to submit OTP request")
                submit()
            }
        }

        binding.goLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    private fun submit() {
        val requestParameters = RequestParameters(
            appId = "APP_118923",
            password = "d400d4dbf74b8fcd0a24eb37cd18db93",
            mobile = number
        )

        val destinationService = ServiceBuilder.buildService(MyApiService::class.java)
        val requestCall = destinationService.requestOtp(requestParameters)

        requestCall.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                Log.d("SignUp", "Entered onResponse")
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        Log.d("SignUp", "OTP sent successfully: $apiResponse")
                        proceedToOtpVerification(apiResponse.referenceNo)
                    } else {
                        Log.e("SignUp", "Response body is null")
                    }
                } else {
                    Log.e("SignUp", "Failed to send OTP: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("SignUp", "Network error: ${t.message}")
            }
        })
    }

    private fun proceedToOtpVerification(referenceNo: String) {
        val intent = Intent(this, otp::class.java).apply {
            putExtra("REFERENCE_NO", referenceNo)
            putExtra("NAME", name)
            putExtra("EMAIL", email)
            putExtra("NUMBER", number)
            putExtra("PASS", pass)
            putExtra("STATUS", status)
        }
        startActivity(intent)
    }
}
