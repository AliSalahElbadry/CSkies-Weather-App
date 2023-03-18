package com.app.our.cskies

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.app.our.cskies.init_setting.InitSetting
import com.app.our.cskies.model.Setting
import com.app.our.cskies.shard_pref.SharedPrefOps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivitySplash : AppCompatActivity() {
   lateinit var lottieAnimationView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        lottieAnimationView = findViewById(R.id.splash_anim)
        lottieAnimationView.playAnimation()
        lifecycleScope.launch(Dispatchers.Main)
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
                SharedPrefOps(applicationContext).loadData()
                val intent = Intent(this@ActivitySplash, ActivityHome::class.java)
                startActivity(intent)
            }
        }
    }
}