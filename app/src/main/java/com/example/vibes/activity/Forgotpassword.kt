package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R
import com.google.firebase.auth.FirebaseAuth

class Forgotpassword : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Get the email input and reset button from the layout
        val emailField = findViewById<EditText>(R.id.evemail)
        val resetButton = findViewById<Button>(R.id.buttonresetpass)

        // Set onClickListener for the reset button
        resetButton.setOnClickListener {
            val email = emailField.text.toString().trim()

            // Check if email is not empty
            if (email.isNotEmpty()) {
                // Send password reset email
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Show success message
                            Toast.makeText(this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show()

                            // Redirect user back to the login screen after sending the email
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Show error message
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Show message if the email field is empty
                Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}