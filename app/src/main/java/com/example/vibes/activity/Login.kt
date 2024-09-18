package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val txtsignup = findViewById<TextView>(R.id.textViewSignup)
        val tvforgotpass = findViewById<TextView>(R.id.tvforgotpass)
        val btnlogin = findViewById<Button>(R.id.buttonLogin)


        txtsignup.setOnClickListener{
            val i =Intent(this, Signup::class.java)
            startActivity(i)
        }

        tvforgotpass.setOnClickListener{
            val i = Intent(this, Forgotpassword::class.java)
            startActivity(i)
        }


        btnlogin.setOnClickListener{
            val i =Intent(this, Chooseartist::class.java)
            startActivity(i)
        }

    }
}