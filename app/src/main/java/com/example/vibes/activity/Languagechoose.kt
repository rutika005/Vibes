package com.example.vibes.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R
import com.example.vibes.databinding.ActivityLanguagechooseBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

@Suppress("DEPRECATION")
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
            goToFragmentHomePage()
        }
    }

    // Display selected languages in TextView
    @SuppressLint("SetTextI18n")
    private fun showSelectedLanguages(group: ChipGroup) {
        val selectedLanguages = mutableListOf<String>()

        // Loop through all chips in the ChipGroup
        for (i in 0 until group.childCount) {
            val chip = group.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedLanguages.add(chip.text.toString())
            }
        }

        // Update the TextView with selected languages
        if (selectedLanguages.isEmpty()) {
            selectedLanguagesTextView.text = "No language selected"
        } else {
            selectedLanguagesTextView.text = "Selected: ${selectedLanguages.joinToString(", ")}"
        }
    }

    // Method to navigate to the next screen
    private fun goToFragmentHomePage() {
        val i= Intent(this,Fragment_Home_Page::class.java)
        startActivity(i)
    }
}