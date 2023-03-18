package com.app.our.cskies

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityHome : AppCompatActivity() {
   lateinit var drawerLayout: DrawerLayout
   lateinit var navigationView: NavigationView
   var index=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        val actionBar = supportActionBar
        drawerLayout = findViewById(R.id.mdrow)
        navigationView = findViewById(R.id.nav_layout)
        actionBar!!.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        val con = findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(navigationView, con)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
               } else {
                drawerLayout.openDrawer(GravityCompat.START)
               }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        if (index == 0) {
            Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT).show()
            index++
            lifecycleScope.launch {
                delay(300)
                index=0
            }
        } else if (index == 1) finishAffinity()
    }

}