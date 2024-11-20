package com.example.vibes.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.vibes.R
import com.example.vibes.databinding.ActivityFragmentHomePageBinding
import com.example.vibes.fragments.FragmentHome
import com.example.vibes.fragments.FragmentLibrary
import com.example.vibes.fragments.FragmentSearch

class Fragment_Home_Page : AppCompatActivity() {
    private lateinit var mBinding: ActivityFragmentHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFragmentHomePageBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        replaceFragment(FragmentHome())

        mBinding.btnHome.setOnClickListener {
            replaceFragment(FragmentHome())
        }

        mBinding.btnsearch.setOnClickListener {
            replaceFragment(FragmentSearch())
        }

        mBinding.btnLibrary.setOnClickListener {
            replaceFragment(FragmentLibrary())
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentDemo, fragment)
        fragmentTransaction.commit()
    }
}