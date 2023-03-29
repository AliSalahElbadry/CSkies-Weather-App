package com.app.our.cskies.home.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.our.cskies.R
import com.app.our.cskies.databinding.ActivityMainBinding
import com.app.our.cskies.home.viewModel.ViewModelHome
import com.app.our.cskies.utils.LanguageUtils
import com.app.our.cskies.utils.Setting
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityMain : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    var index=0
    lateinit var model:ViewModelHome
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageUtils.setAppLocale(Setting.getLang(),this)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)
        val actionBar = supportActionBar
        actionBar!!.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowTitleEnabled(true)
        val navController: NavController = Navigation.findNavController(this, R.id.my_host_fragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
           supportFragmentManager.popBackStack(controller.previousBackStackEntry?.id,FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        setupWithNavController(binding.navigator, navController = navController)
        val appBarConfiguration= AppBarConfiguration(navController.graph,binding.drowerLayout)
        binding.myToolbar.setupWithNavController(navController,appBarConfiguration)
        binding.navigator.setupWithNavController(navController)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (binding.drowerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drowerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drowerLayout.openDrawer(GravityCompat.START)
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