package com.app.our.cskies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.app.our.cskies.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityMain : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    var index=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        val con: NavController = Navigation.findNavController(this, R.id.my_host_fragment)
        setupWithNavController(binding.navLayout, navController = con)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (binding.mdrow.isDrawerOpen(GravityCompat.START)) {
                    binding.mdrow.closeDrawer(GravityCompat.START)
                } else {
                    binding.mdrow.openDrawer(GravityCompat.START)
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