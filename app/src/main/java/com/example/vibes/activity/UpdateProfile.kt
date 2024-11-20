package com.example.vibes.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R
import com.example.vibes.databinding.ActivityUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfile : AppCompatActivity() {

    private lateinit var mBinding: ActivityUpdateProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private var selectedGender: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setupGenderSpinner()
        fetchUserData()

        // Back Button Event
        mBinding.backbutton.setOnClickListener {
            finish()
        }

        // Update Button Event
        mBinding.updateButton.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun setupGenderSpinner() {
        val genders = resources.getStringArray(R.array.gender_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.inputGender.adapter = adapter

        mBinding.inputGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGender = genders[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedGender = ""
            }
        }
    }

    private fun fetchUserData() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            firestore.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username = document.getString("userName")
                        val email = document.getString("email")
                        val phoneNumber = document.getString("phoneNumber")
                        val gender = document.getString("gender")

                        // Set data in the fields
                        mBinding.inputName.setText(username)
                        mBinding.inputEmail.setText(email)
                        mBinding.inputPhoneNumber.setText(phoneNumber ?: "")

                        // Preselect gender
                        if (gender != null) {
                            val genderIndex = resources.getStringArray(R.array.gender_array).indexOf(gender)
                            if (genderIndex >= 0) {
                                mBinding.inputGender.setSelection(genderIndex)
                            }
                        }
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("UpdateProfile", "Error fetching user data", exception)
                    Toast.makeText(this, "Failed to load profile data", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserProfile() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val updatedData = hashMapOf(
                "phoneNumber" to mBinding.inputPhoneNumber.text.toString().trim(),
                "gender" to selectedGender
            )

            firestore.collection("Users").document(userId).update(updatedData as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("UpdateProfile", "Error updating profile", exception)
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}