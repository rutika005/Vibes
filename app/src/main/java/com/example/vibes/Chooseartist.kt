package com.example.vibes

import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vibes.adapter.GridAdapter

class Chooseartist : AppCompatActivity() {

    lateinit var gridView: GridView
    private var numberNames = arrayOf(
        "One", "Two", "Three", "Four", "Five",
        "Six", "Seven", "Eight", "Nine", "Ten"
    )
    private var numberImages = intArrayOf(
        R.drawable.atif_aslam,
        R.drawable.kk,
        R.drawable.atif_aslam,
        R.drawable.kk,
        R.drawable.atif_aslam,
        R.drawable.kk,
        R.drawable.atif_aslam,
        R.drawable.kk,
        R.drawable.atif_aslam,
        R.drawable.kk,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chooseartist)

        title = "Grid View"

        gridView = findViewById(R.id.gridView)

        val mainAdapter = GridAdapter(
            context = this,
            numbersInWords = numberNames,
            numberImage = numberImages
        )

        gridView.adapter = mainAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                applicationContext, "You CLicked " + numberNames[+position],
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}