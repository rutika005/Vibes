package com.example.vibes.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vibes.R
import com.example.vibes.databinding.ActivityEdituserprofileBinding
import com.example.vibes.fragments.Fragment_home

class Edituserprofile : AppCompatActivity() {
    private lateinit var mBinding: ActivityEdituserprofileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edituserprofile)
        mBinding = ActivityEdituserprofileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.backArrow.setOnClickListener(btnImgEvents)
        mBinding.editprofilebutton.setOnClickListener(btnbuttonEvent)

    }
    private val btnImgEvents = View.OnClickListener { view ->
        when (view.id) {
            R.id.backArrow -> goToHometype()
        }
    }
    private val btnbuttonEvent = View.OnClickListener { view ->
        when (view.id){
            R.id.editprofilebutton -> goToUpdateprofile()
        }
    }

    private fun goToHometype() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replace the current fragment with Fragment_home
        fragmentTransaction.replace(R.id.fragment_container, Fragment_home())

        // Optional: Add the transaction to the back stack
        fragmentTransaction.addToBackStack(null)

        // Commit the transaction
        fragmentTransaction.commit()
    }

    private fun goToUpdateprofile(){
        val i = Intent(this,UpdateProfile::class.java)
        startActivity(i)
    }
}