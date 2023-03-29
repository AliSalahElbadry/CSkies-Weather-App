package com.app.our.cskies.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.app.our.cskies.home.view.ActivityMain
import com.app.our.cskies.R
import com.app.our.cskies.init_setting.InitSetting
import com.app.our.cskies.shard_pref.SharedPrefOps
import com.app.our.cskies.utils.Dialogs
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserStates
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivitySplash : AppCompatActivity(), SplashCall {
   lateinit var lottieAnimationView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        lottieAnimationView = findViewById(R.id.splash_anim)
        lottieAnimationView.playAnimation()
        val shp=SharedPrefOps(applicationContext)
        val isLoadingSetting=shp.isNewSettingRestart()
        shp.saveAsNewSetting(-1)
        if(isLoadingSetting==1)
        {
            Dialogs.SnakeToast(findViewById(R.id.constarin_splash),
                if(Setting.getLang()=="en")
                    "Loading New Setting ..."
                else
                    "يتم تحميل الاعدادات الجديدة"
            )
        }
        lifecycleScope.launch()
        {
            launch {
                delay(3000)
            }.join()
            val initSetting=getSharedPreferences("setting",Context.MODE_PRIVATE)
            if(!initSetting.contains("location")&&UserStates.checkConnectionState(this@ActivitySplash))
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