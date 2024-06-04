package com.example.communitystay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class viewinonepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewinonepage)

        var text=intent.getStringExtra("Complain text")
        findViewById<TextView>(R.id.complainfulltext).setText(text)
    }
}