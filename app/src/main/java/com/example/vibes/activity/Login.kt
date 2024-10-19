package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.vibes.R
import com.example.vibes.databinding.ActivityEdituserprofileBinding
import com.example.vibes.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()

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

    private fun goToChooseartist(){
        val i =Intent(this, Chooseartist::class.java)
        startActivity(i)
    }
}