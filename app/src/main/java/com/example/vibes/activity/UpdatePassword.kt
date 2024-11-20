package com.example.vibes.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class UpdatePassword : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        // Initialize Firebase Authentication and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        firestore = FirebaseFirestore.getInstance()  // Firestore initialization
 
        // Handle Firebase Dynamic Link if the user clicks a reset password link
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                val deepLink: Uri? = pendingDynamicLinkData?.link
                if (deepLink != null) {
                    // Use the deep link to navigate or perform specific actions
                    Log.d("DynamicLink", "Received dynamic link: $deepLink")
                }
            }
            .addOnFailureListener(this) { e ->
                Log.w("DynamicLink", "getDynamicLink:onFailure", e)
            }

        // UI elements
        val newPasswordField = findViewById<EditText>(R.id.evNewPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.evConfirmPassword)
        val updatePasswordButton = findViewById<Button>(R.id.buttonUpdatePassword)

        updatePasswordButton.setOnClickListener {
            val newPassword = newPasswordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (newPassword == confirmPassword) {
                    // Update the password for the current user
                    currentUser.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Password successfully updated in Firebase Auth
                                Toast.makeText(this, "Password updated successfully.", Toast.LENGTH_SHORT).show()

                                // Optional: Update Firestore if there's any user info to change
                                updateFirestoreUserPassword(newPassword)

                                finish() // Close the activity after success
                            } else {
                                Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Updates the password or other information in Firestore (optional step).
     */
    private fun updateFirestoreUserPassword(newPassword: String) {
        val userId = currentUser.uid
        val userRef = firestore.collection("Users").document(userId)

        // Update password field in Firestore (optional if you store password for reference)
        val updates = hashMapOf<String, Any>(
            "password" to newPassword // You can also update other fields if necessary
        )

        userRef.update(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Password updated in Firestore.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}