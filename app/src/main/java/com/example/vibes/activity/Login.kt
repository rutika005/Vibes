package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.vibes.R
import com.example.vibes.databinding.ActivityEdituserprofileBinding
import com.example.vibes.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        mBinding.textViewSignup.setOnClickListener(textViewSignup)
        mBinding.tvforgotpass.setOnClickListener(tvforgotpass)
        mBinding.buttonLogin.setOnClickListener(buttonLogin)

    }

    private val textViewSignup = View.OnClickListener { view ->
        when (view.id) {
            R.id.textViewSignup -> goToSignup()
        }
    }
    private val tvforgotpass = View.OnClickListener { view ->
        when (view.id) {
            R.id.tvforgotpass -> goToForgotpass()
        }
    }
    private val buttonLogin = View.OnClickListener { view ->
        when (view.id) {
            R.id.buttonLogin -> goToChooseartist()
        }
    }

    private fun goToSignup(){

        val i =Intent(this, Signup::class.java)
        startActivity(i)
    }

    private fun goToForgotpass(){
        val i = Intent(this, Forgotpassword::class.java)
        startActivity(i)
    }

    private fun goToChooseartist() {
        val userName = mBinding.editTextFullName.text.toString() // Username field
        val password = mBinding.editTextPassword.text.toString() // Password field

        if (userName.isNotEmpty() && password.isNotEmpty()) {
            // Step 1: Retrieve user data from Firebase Realtime Database based on the entered username
            database.reference.child("UserSignIn").child("Users").orderByChild("userName").equalTo(userName).get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        // Assuming userName is unique and retrieves the correct user's data
                        val userMap = dataSnapshot.children.first().value as Map<*, *>
                        val registeredUserName = userMap["userName"] as? String // Get the stored username
                        val registeredPassword = userMap["password"] as? String // Get the stored password

                        // Step 2: Compare the entered password with the stored password
                        if (password == registeredPassword && userName == registeredUserName) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                            // Navigate to the next screen
                            val intent = Intent(this, Chooseartist::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Login failed: Incorrect credentials.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Username not found.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // If there's an error retrieving data, show the specific error message
                    Toast.makeText(this, "Failed to retrieve user data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
        }
    }
}