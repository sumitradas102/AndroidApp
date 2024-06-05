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
                // Send OTP
                sendOTP()
            }
        }

        binding.goLogin.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        }
    }

    private fun sendOTP() {
        // TODO: Integrate OTP service to send OTP

        // For now, directly navigate to OTP verification page
        val intent = Intent(this,otp::class.java)
        intent.putExtra("NAME", name)
        intent.putExtra("EMAIL", email)
        intent.putExtra("NUMBER", number)
        intent.putExtra("PASS", pass)
        intent.putExtra("STATUS", status)
        startActivity(intent)
    }

    // Other functions remain unchanged
}
