package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R
import com.example.vibes.adapter.GridAdapter

class Chooseartist : AppCompatActivity() {

    private lateinit var gridView: GridView
    private var numberNames = arrayOf(
        "Atif Aslam",
        "KK",
        "Atif Aslam",
        "KK",
        "Atif Aslam",
        "KK",
        "Atif Aslam",
        "KK",
        "Atif Aslam",
        "KK",
        "Atif Aslam",
        "KK",
        "Atif Aslam",
        "KK",
        "Atif Aslam",
        "KK",
        "Atif Aslam",
        "KK"
    )

    private var numberImages = intArrayOf(
        R.drawable.atif_aslam, R.drawable.kk, R.drawable.atif_aslam, R.drawable.kk,
        R.drawable.atif_aslam, R.drawable.kk, R.drawable.atif_aslam, R.drawable.kk,
        R.drawable.atif_aslam, R.drawable.kk, R.drawable.atif_aslam, R.drawable.kk,
        R.drawable.atif_aslam, R.drawable.kk, R.drawable.atif_aslam, R.drawable.kk,
        R.drawable.atif_aslam, R.drawable.kk,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooseartist)

        gridView = findViewById(R.id.gridView)

        val mainAdapter = GridAdapter(
            context = this,
            numbersInWords = numberNames,
            numberImage = numberImages
        )
        gridView.adapter = mainAdapter

        val btnartistnext = findViewById<Button>(R.id.btnartistnext)
        val backArrow = findViewById<ImageView>(R.id.backarr)
        val key = intent.getBooleanExtra("isFromEditUser", false)
        if (key) {
            btnartistnext.visibility = View.INVISIBLE
            backArrow.visibility = View.VISIBLE
        }

        btnartistnext.setOnClickListener {
            val i = Intent(this, Musictype::class.java)
            startActivity(i)

        }

        backArrow.setOnClickListener {
            finish()

        }
    }
}