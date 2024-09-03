package com.example.vibes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Forgotpassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgotpassword)
        val buttonreset = findViewById<Button>(R.id.buttonresetpass)

        buttonreset.setOnClickListener{
            val i = Intent(this,Login::class.java)
            startActivity(i)
        }

    }
}