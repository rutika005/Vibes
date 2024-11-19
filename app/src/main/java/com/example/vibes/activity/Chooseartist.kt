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
        "Talha Anjum",
        "Charlie Puth",
        "Darshan Raval",
        "Nova",
        "KK",
        "Mohit Chauhan",
        "Sonu Nigam",
        "Anuv Jain",
        "Shreya Ghoshal",
        "Aditya Rikhari",
        "Aditya Gadhvi",
        "Armaan Malik",
        "Jigardan Gadhvi",
        "Young Stunners",
        "Arijit Singh",
        "Selena Gomez",
        "Taylor Swift"
    )

    private var numberImages = intArrayOf(
        R.drawable.atif_aslam, R.drawable.talha_arjum, R.drawable.charli, R.drawable.darshan,
        R.drawable.nova, R.drawable.kk, R.drawable.mohit_chauhan, R.drawable.sonu_nigam,
        R.drawable.anuv_jain, R.drawable.shreya_ghoshal, R.drawable.aditya_rikhari, R.drawable.aditya_gadhvi,
        R.drawable.armaan_malik, R.drawable.jigardan_gadhvi, R.drawable.young_stunners,R.drawable.arijit_singh,
        R.drawable.selena_gomez, R.drawable.taylor_swift_1,
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