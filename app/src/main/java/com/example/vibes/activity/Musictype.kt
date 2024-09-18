package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R

class Musictype : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_musictype)

        val iconback = findViewById<ImageButton>(R.id.iconback)



        iconback.setOnClickListener{
            val i = Intent(this, Chooseartist::class.java)
            startActivity(i)
        }


    }
}