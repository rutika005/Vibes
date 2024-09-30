package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R

class Signuplogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signuplogin)
        var signup = findViewById<Button>(R.id.btnsignup)
        var login = findViewById<Button>(R.id.btnlogin)

        signup.setOnClickListener{
            val i= Intent(this, Signup::class.java)
            startActivity(i)
        }
        login.setOnClickListener{
            val i=Intent(this, Login::class.java)
            startActivity(i)
        }

    }
}