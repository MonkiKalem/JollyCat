package com.example.jollycat

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest

class MainActivity : AppCompatActivity() {
    private val loadingTime: Long = 2000 // 2 seconds

    private val permissions = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (!isGranted) {
                    // Handle the case where the permission is not granted
                    // You can show a dialog explaining why the permission is needed and ask again
                }
                else{
                    loading()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!hasPermissions()){
            requestPermissions()

        } else {
            loading()
        }

    }

    private  fun loading() {
        // Delay for a certain time to simulate loading
        Handler().postDelayed({
            // Start LoginActivity after delay
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Finish MainActivity to prevent returning to it
        }, loadingTime)
    }

    private fun hasPermissions(): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(permissions)
    }

}
