package com.example.vibes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val getstarted = findViewById<Button>(R.id.btngetstarted)
        getstarted.setOnClickListener{
            val i = Intent(this,Signuplogin::class.java)
            startActivity(i)
        }
    }
}
