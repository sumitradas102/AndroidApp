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
import com.example.communitystay.OtpVerifyRespone
import com.example.communitystay.VerifyParameters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class otp : AppCompatActivity() {

    private lateinit var otpEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var referenceNo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        otpEditText = findViewById(R.id.otp)
        submitButton = findViewById(R.id.buttonotp)

        referenceNo = intent.getStringExtra("REFERENCE_NO") ?: ""

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
            appId = "APP_119062",
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
                        navigateToMainActivity2()
                    } else {
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

    private fun navigateToMainActivity2() {
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }
}


