package com.example.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.drawee.backends.pipeline.Fresco
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var editUsername: EditText
    lateinit var editPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonRegister: Button
    var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeComponents()
    }

    private fun initializeComponents() {
        editUsername = findViewById<EditText>(R.id.editUsername)
        editPassword = findViewById<EditText>(R.id.editPassword)
        buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonRegister = findViewById<Button>(R.id.buttonRegister)

        url = "http://192.168.1.40:9000/api/customer/login"


        buttonLogin.setOnClickListener {
            signin(editUsername.text.toString(), editPassword.text.toString())
        }

        buttonRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signin(username: String, password: String) {
        val queue = Volley.newRequestQueue(this)

        var jsonParams: JSONObject = JSONObject();
        jsonParams.put("username", username)
        jsonParams.put("password", password)

        // Building a request
        val postRequest = JsonObjectRequest(
            Request.Method.POST,
            url, jsonParams,
            Response.Listener { response ->
                Log.d("Response", response.toString())
                var intent = Intent(this, WelcomeActivity::class.java)
                intent.putExtra("response", response.toString())
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                Log.d("Response", "Error: " + error.message)
                Toast.makeText(this.applicationContext, "Error: " + error.message, Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(postRequest)
    }
}