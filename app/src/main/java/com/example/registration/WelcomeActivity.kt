package com.example.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.json.JSONObject

class WelcomeActivity : AppCompatActivity() {
    lateinit var textViewFullName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        textViewFullName = findViewById<TextView>(R.id.textViewFullname)
        var intent = intent
        val stringResponse = intent.getStringExtra("response")
        if(stringResponse != null){
//            var jsonObject: JSONObject = JSONObject(stringResponse)
//            val fullname:String = jsonObject.get("fullname").toString()
//            textViewFullName.text = fullname
        }else{
            textViewFullName.text = "No Name"
        }

    }
}