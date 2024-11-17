package com.example.vibes.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val getstarted = findViewById<Button>(R.id.btngetstarted)

        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        getstarted.setOnClickListener {

            if (isLoggedIn) {
                val intent = Intent(this, Fragment_Home_Page::class.java)
                startActivity(intent)
                finish() // Prevent going back to the login screen
            } else {
                val i = Intent(this, Signuplogin::class.java)
                startActivity(i)
            }

        }
    }
}