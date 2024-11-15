package com.example.vibes.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.vibes.R

class Signuplogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signuplogin)

        init()

        var signup = findViewById<Button>(R.id.btnsignup)
        var login = findViewById<Button>(R.id.btnlogin)

        signup.setOnClickListener{
            val i= Intent(this, Signup::class.java)
            startActivity(i)
        }
        login.setOnClickListener{
            val i=Intent(this, Login::class.java)
            startActivity(i)
        }

    }

    private fun init(){
        permisionNotification()
    }

    private fun permisionNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is granted. You can post notifications.
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Show an explanation to the user *asynchronously*
                    // After the user sees the explanation, try again to request the permission.
                }
                else -> {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted. Proceed with posting notifications.
            } else {
                // Explain to the user that the feature is unavailable because the permission was denied.
            }
        }

}