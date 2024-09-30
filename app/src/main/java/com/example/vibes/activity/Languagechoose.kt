package com.example.vibes.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.example.vibes.R
import com.example.vibes.databinding.ActivityFragmentHomePageBinding
import com.example.vibes.databinding.ActivityLanguagechooseBinding
import com.example.vibes.fragments.Fragment_home
import com.google.android.material.chip.ChipGroup

class Languagechoose : AppCompatActivity() {

    private lateinit var mBinding: ActivityLanguagechooseBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languagechoose)
        mBinding = ActivityLanguagechooseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.icBack.setOnClickListener(btnClickEvents)
        mBinding.buttonnext.setOnClickListener(btnClickEvents)

        val chipGroupChoice = findViewById<ChipGroup>(R.id.ChipGroup)
        chipGroupChoice.setOnCheckedChangeListener { group, checkedId ->
            val chip: Chip? = group.findViewById(checkedId)
            chip?.let { chipView ->
                Toast.makeText(this, chipView.text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val btnClickEvents = View.OnClickListener { view ->
        when (view.id) {
            R.id.ic_back -> goToMusictype()
            R.id.buttonnext -> goToFragment_Home_Page()
        }
    }

    private fun goToMusictype() {
        val fIntent = Intent(this, Musictype::class.java)
        startActivity(fIntent)
    }

    private fun goToFragment_Home_Page() {
        val fIntent = Intent(this, Fragment_Home_Page::class.java)
        startActivity(fIntent)
    }
}