package com.example.jollycat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AboutUsActivity : AppCompatActivity() {

    private lateinit var map: GoogleMap
    private lateinit var burgerIcon: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        burgerIcon = findViewById(R.id.burgerIcon)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            setupMap()
        }

        // Set up PopupMenu
        burgerIcon.setOnClickListener { view ->
            showPopupMenu(view)
        }

    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_main)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_dashboard -> {
                    // Handle dashboard action
                    navigateToDashboard()
                    true
                }
                R.id.action_about_us -> {
                    // Handle about us action
                    navigateToAboutUs()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAboutUs() {
        val intent = Intent(this, AboutUsActivity::class.java)
        startActivity(intent)
    }

    private fun setupMap() {
        val jollyCatLocation = LatLng(-6.20175, 106.78208)
        val markerOptions = MarkerOptions().position(jollyCatLocation).title("JollyCat's Store")
        map.addMarker(markerOptions)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(jollyCatLocation, 15f))
    }
}
