@file:Suppress("DEPRECATION")

package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vibes.R
import com.example.vibes.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleSignIn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Initialize Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Replace with your actual client ID
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set up onClickListeners for buttons
        mBinding.textViewSignup.setOnClickListener { goToSignup() }
        mBinding.tvforgotpass.setOnClickListener { goToForgotpass() }
        mBinding.buttonLogin.setOnClickListener { loginUserWithEmail() }
        mBinding.imggoogle.setOnClickListener { signInWithGoogle() } // Google Sign-In button
    }

    private fun goToSignup() {
        val i = Intent(this, Signup::class.java)
        startActivity(i)
    }

    private fun goToForgotpass() {
        val i = Intent(this, Forgotpassword::class.java)
        startActivity(i)
    }

    private fun loginUserWithEmail() {
        val email = mBinding.editTextEmail.text.toString()
        val password = mBinding.editTextPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Firebase Authentication: Sign in with email and password
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, Chooseartist::class.java)
                        startActivity(intent)
                        finish() // Close the login activity
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        RC_SIGN_IN
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign-in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Google Sign-In success
                    val user = firebaseAuth.currentUser
                    Toast.makeText(this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show()

                    // Navigate to Chooseartist activity
                    val intent = Intent(this, Chooseartist::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this,
                        "Google Sign-In Failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}