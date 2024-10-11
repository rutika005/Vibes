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
import com.example.vibes.databinding.ActivityUpdateProfileBinding
import com.example.vibes.fragments.Fragment_home

class UpdateProfile : AppCompatActivity() {
    private lateinit var mBinding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_profile)

        mBinding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.backbutton.setOnClickListener(btnImgEvents)
    }
    private val btnImgEvents = View.OnClickListener { view ->
        when (view.id) {
            R.id.backbutton -> goToEdituser()
        }
    }

    private fun goToEdituser() {
        val i = Intent(this,Edituserprofile::class.java)
        startActivity(i)
    }
}