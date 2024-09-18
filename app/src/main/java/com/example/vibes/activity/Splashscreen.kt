package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R

class Splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreen)
        Handler().postDelayed(Runnable {
            try {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            } catch (e: Exception) {
                Log.e("Splashscreen", "Error starting MainActivity", e)
            }
        }, 3000)
    }
}