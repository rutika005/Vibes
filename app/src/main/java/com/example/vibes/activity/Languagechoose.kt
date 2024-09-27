package com.example.vibes.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.example.vibes.R
import com.google.android.material.chip.ChipGroup

class Languagechoose : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languagechoose)

        val ic_back = findViewById<ImageButton>(R.id.ic_back)

        ic_back.setOnClickListener{
            val i = Intent(this, Musictype::class.java)
            startActivity(i)
        }

        val chipGroupChoice = findViewById<ChipGroup>(R.id.ChipGroup)
        chipGroupChoice.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip? = group.findViewById(checkedId)
            chip?.let { chipView ->
                Toast.makeText(this, chipView.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}