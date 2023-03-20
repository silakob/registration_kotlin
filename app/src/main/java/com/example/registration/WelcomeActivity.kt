package com.example.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class WelcomeActivity : AppCompatActivity() {
    lateinit var textViewFullName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        textViewFullName = findViewById<TextView>(R.id.textViewFullname)
        var intent = Intent(this, WelcomeActivity::class.java)
        textViewFullName.text = intent.getStringExtra("fullName")
    }
}