package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.vibes.R

class Musictype : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_musictype)

        addClickListener()
    }

    private fun addClickListener() {
        val nextButton = findViewById<Button>(R.id.nextbutton)
        val pop = findViewById<Button>(R.id.btnpop)
        val btnhiphop = findViewById<Button>(R.id.btnhiphop)
        val btnrandb = findViewById<Button>(R.id.btnrandb)
        val btnblues = findViewById<Button>(R.id.btnblues)
        val btnlatin = findViewById<Button>(R.id.btnlatin)
        val btnrock = findViewById<Button>(R.id.btnrock)
        val btndance = findViewById<Button>(R.id.btndance)
        val btnfolk = findViewById<Button>(R.id.btnfolk)
        val btnelectro = findViewById<Button>(R.id.btnelectro)
        val btnkpop = findViewById<Button>(R.id.btnkpop)

        // List of all buttons
        val allButtons = listOf(
            pop,
            btnhiphop,
            btnrandb,
            btnblues,
            btnlatin,
            btnrock,
            btndance,
            btnfolk,
            btnelectro,
            btnkpop
        )

        // Navigate to another activity
        nextButton.setOnClickListener {
            val i = Intent(this, Languagechoose::class.java)
            startActivity(i)
        }

        // Assign click listeners to all buttons
        allButtons.forEach { button ->
            button.setOnClickListener {
                // Highlight the clicked button
                button.background =
                    ContextCompat.getDrawable(this, R.drawable.rounded_button_background_selected)

                // Reset backgrounds of other buttons
                allButtons.filter { it != button }.forEach { otherButton ->
                    otherButton.background =
                        ContextCompat.getDrawable(this, R.drawable.rounded_button_background)
                }
            }
        }
    }
}