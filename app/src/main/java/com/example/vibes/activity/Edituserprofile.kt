package com.example.vibes.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R
import com.example.vibes.databinding.ActivityEdituserprofileBinding
import com.example.vibes.fragments.Fragment_home
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class Edituserprofile : AppCompatActivity() {
    private lateinit var mBinding: ActivityEdituserprofileBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edituserprofile)
        mBinding = ActivityEdituserprofileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Initialize Firebase Auth and Google Sign-In client
        firebaseAuth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        // Set up click listeners
        mBinding.backArrow.setOnClickListener(btnImgEvents)
        mBinding.editprofilebutton.setOnClickListener(btnbuttonEvent)
        mBinding.btnLogout.setOnClickListener { showLogoutDialog() } // Logout button click
    }

    private val btnImgEvents = View.OnClickListener { view ->
        when (view.id) {
            R.id.backArrow -> goToHometype()
        }
    }

    private val btnbuttonEvent = View.OnClickListener { view ->
        when (view.id) {
            R.id.editprofilebutton -> goToUpdateprofile()
        }
    }

    private fun goToHometype() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replace the current fragment with Fragment_home
        fragmentTransaction.replace(R.id.fragment_container, Fragment_home())
        fragmentTransaction.addToBackStack(null) // Optional: Add to back stack
        fragmentTransaction.commit()
    }

    private fun goToUpdateprofile() {
        val i = Intent(this, UpdateProfile::class.java)
        startActivity(i)
    }

    // Function to show logout confirmation dialog
    private fun showLogoutDialog() {
        // Create an AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Log out")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            logoutUser() // Call logout function when user clicks "Yes"
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Close the dialog when user clicks "No"
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    // Logout function: sign out from Firebase and Google
    private fun logoutUser() {
        // Sign out from Firebase
        firebaseAuth.signOut()

        // Sign out from Google
        googleSignInClient.signOut().addOnCompleteListener {
            // After signing out, navigate to Login activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Close the current activity
        }
    }
}