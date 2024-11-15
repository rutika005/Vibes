package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R

class Musictype : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_musictype)

        val nextButton = findViewById<Button>(R.id.nextbutton)

        nextButton.setOnClickListener{
            val i = Intent(this, Languagechoose::class.java)
            startActivity(i)
        }

    }
}