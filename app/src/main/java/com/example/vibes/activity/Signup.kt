package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {
    private lateinit var mBinding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        mBinding.textViewLogin.setOnClickListener {
            goToLogin()
        }

        mBinding.buttonSignUp.setOnClickListener {
            signUpUser()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    private fun signUpUser() {
        val fullName = mBinding.editTextFullName.text.toString()
        val userName = mBinding.editTextUsername.text.toString()
        val email = mBinding.editTextEmail.text.toString()
        val password = mBinding.editTextPassword.text.toString()

        if (fullName.isNotEmpty() && userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            // Create user in Firebase Authentication
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get the current user
                        val firebaseUser = firebaseAuth.currentUser
                        firebaseUser?.let { user ->
                            val uid = user.uid
                            val userData = linkedMapOf(
                                "userName" to userName,
                                "password" to password,
                                "uid" to uid,
                                "fullName" to fullName,
                                "email" to email
                            )

                            // Save user data in Firebase Realtime Database
                            database.reference.child("UserSignIn").child("Users").setValue(userData)
                                .addOnCompleteListener { saveTask ->
                                    if (saveTask.isSuccessful) {
                                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                        // Navigate to login page
                                        val intent = Intent(this, Login::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Failed to save user data: ${saveTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(this, "Error saving data: ${exception.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        // Handle sign-up failure
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            // Email already registered
                            Toast.makeText(this, "This email is already registered. Please login or use a different email.", Toast.LENGTH_LONG).show()
                        } else {
                            // Other registration failures
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Authentication failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Show error if fields are empty
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
        }
    }
}
