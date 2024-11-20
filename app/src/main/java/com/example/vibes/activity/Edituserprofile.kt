package com.example.vibes.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.databinding.ActivityEdituserprofileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Edituserprofile : AppCompatActivity() {
    private lateinit var mBinding: ActivityEdituserprofileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityEdituserprofileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Initialize Firebase instances
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Fetch and display username
        fetchAndDisplayUsername()

        // Set up click listeners
        mBinding.backarr.setOnClickListener { finish() }
        mBinding.editprofilebutton.setOnClickListener { goToUpdateProfile() }
        mBinding.artistsbutton.setOnClickListener { goToSelectArtist() }
        mBinding.btnLogout.setOnClickListener { showLogoutDialog() }

        mBinding.artistsbutton.setOnClickListener{
            goToSelectArtist()
        }
        mBinding.btnLogout.setOnClickListener { showLogoutDialog() }

        val sharedPreferences = getSharedPreferences("SelectedItems", Context.MODE_PRIVATE)
        val artistName = sharedPreferences.getString("artistName", null)

        mBinding.artistsbutton.text = artistName

    }

    private fun fetchAndDisplayUsername() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            firestore.collection("Users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username = document.getString("userName")
                        mBinding.name.text = username ?: "Name not found"
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("EditUserProfile", "Error fetching username", exception)
                    Toast.makeText(this, "Failed to fetch username", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToUpdateProfile() {
        val intent = Intent(this, UpdateProfile::class.java)
        startActivity(intent)
    }

    private fun goToSelectArtist() {
        val intent = Intent(this, Chooseartist::class.java)
        intent.putExtra("isFromEditUser", true)
        startActivity(intent)
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { dialog, _ ->
                logoutUser()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun logoutUser() {
        firebaseAuth.signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}