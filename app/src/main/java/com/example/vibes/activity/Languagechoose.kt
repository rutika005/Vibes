package com.example.vibes.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R
import com.example.vibes.databinding.ActivityLanguagechooseBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class Languagechoose : AppCompatActivity() {

    private lateinit var mBinding: ActivityLanguagechooseBinding
    private lateinit var selectedLanguagesTextView: TextView // TextView to show selected languages

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLanguagechooseBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Initialize the TextView that displays selected languages
        selectedLanguagesTextView = findViewById(R.id.selected_languages_text)

        // Handle selection changes in ChipGroup
        mBinding.ChipGroup.setOnCheckedChangeListener { group, _ ->
            showSelectedLanguages(group)
        }

        // Button click event to move to the next screen
        mBinding.buttonnext.setOnClickListener {
            goToFragment_Home_Page()
        }
    }

    // Display selected languages in TextView
    private fun showSelectedLanguages(group: ChipGroup) {
        val selectedLanguages = mutableListOf<String>()

        for (i in 0 until group.childCount) {
            val chip = group.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedLanguages.add(chip.text.toString())
            }
        }

        if (selectedLanguages.isEmpty()) {
            selectedLanguagesTextView.text = "No language selected"
        } else {
            selectedLanguagesTextView.text = "Selected: ${selectedLanguages.joinToString(", ")}"
        }
    }

    // Method to navigate to the next screen
    private fun goToFragment_Home_Page() {
        // Your intent code here
    }
}