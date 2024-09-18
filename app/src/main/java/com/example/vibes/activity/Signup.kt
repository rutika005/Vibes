package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        val txtlogin = findViewById<TextView>(R.id.textViewLogin)
        val signup = findViewById<Button>(R.id.buttonSignUp)

        txtlogin.setOnClickListener{
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }
    }
}