package com.example.vibes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

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