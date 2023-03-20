package com.app.our.cskies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.app.our.cskies.init_setting.InitSetting
import com.app.our.cskies.shard_pref.SharedPrefOps
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivitySplash : AppCompatActivity(),SplashCall {
   lateinit var lottieAnimationView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        lottieAnimationView = findViewById(R.id.splash_anim)
        lottieAnimationView.playAnimation()
        lifecycleScope.launch()
        {
            launch {
                delay(3000)
            }.join()
            val initSetting=getSharedPreferences("setting",Context.MODE_PRIVATE)
            if(!initSetting.contains("location"))
            {
                val initFragment = InitSetting()
                initFragment.isCancelable = false
                val manager = supportFragmentManager
                initFragment.show(manager,null)
            }else {
               val pref= SharedPrefOps(applicationContext)
                pref.loadData()
                pref.loadLocationData()
                showHome()
            }
        }
    }

    override fun showHome() {
        val intent = Intent(this@ActivitySplash, ActivityMain::class.java)
        startActivity(intent)
    }
}