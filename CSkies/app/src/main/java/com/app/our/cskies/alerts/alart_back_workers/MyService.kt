package com.app.our.cskies.alerts.alart_back_workers

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.RemoteViews
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.our.cskies.R
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.alerts.utils.AlarmUtils
import com.app.our.cskies.dp.LocalSourceImpl
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.network.ApiState
import com.app.our.cskies.network.RemoteSourceImpl
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserCurrentLocation
import com.app.our.cskies.utils.UserStates
import kotlinx.coroutines.*

class MyService : Service() {

    private val notificationId: Int=1997169
    private var CHANNEL_ID: String="channel_1234_Skies"
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alert = intent?.getSerializableExtra("alarm") as Alert
        val repo = Repository.getInstance(LocalSourceImpl.getInstance(this), RemoteSourceImpl.getInstance()!!)
        if (UserStates.checkConnectionState(this)) {
            CoroutineScope(Dispatchers.IO).launch {
                repo.getWeatherData("40.847807", "74.574725", lang = Setting.getLang()).collect { apiState ->
                    when (apiState) {
                        is ApiState.Success -> {
                            if (apiState.locationData.alerts != null) {
                                val data = apiState.locationData
                                var msg = ""
                                msg += "${data.lat}\n${data.lon}"
                                UserCurrentLocation.longitude = data.lon.toString()
                                UserCurrentLocation.latitude = data.lat.toString()
                                msg += "\n ${
                                    UserCurrentLocation.getAddress(
                                        this@MyService,
                                        false
                                    )
                                }"
                                msg += "\n"
                                data.alerts.forEach {
                                    msg += "${it.event}\t"
                                    msg += "${it.description}\n"
                                }
                                withContext(Dispatchers.Main) {
                                    showAlarmOrNotification(
                                        msg,
                                        alert.type
                                    )
                                }

                            } else {
                                withContext(Dispatchers.Main) {
                                    showAlarmOrNotification(
                                        if (Setting.getLang() == "en")
                                            "There Is No Weather Alerts In ${alert.address} for Today                       0                 0                    0"
                                        else
                                            "لا يوجد تنبيهات طقس  ${alert.address} اليوم ",
                                        alert.type
                                    )
                                }
                            }
                        }
                        is ApiState.Failure -> {

                        }
                        else -> {

                        }
                    }
                }
            }
        }else{
                showAlarmOrNotification(
                    if (Setting.getLang() == "en")
                        "There Is No Weather Alerts In ${alert.address} for Today"
                    else
                        "لا يوجد تنبيهات طقس  ${alert.address}اليوم ",
                    alert.type
                )
        }
        if (alert.numOfDays > 0) {
            alert.numOfDays -= 1
            alert.fromDate=(alert.fromDate.toLong()+86400000).toString()
            AlarmUtils.setAlarm(applicationContext, alert)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                repo.deleteAlert(alert.getId()!!)
            }
            AlarmUtils.canelAlarm(applicationContext,alert)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.alarm)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }


    @SuppressLint("MissingPermission")
    private fun showAlarmOrNotification(msg: String, type: Int) {
        if (type == 0) {
            val player=MediaPlayer.create(this@MyService,R.raw.alarm)
            player.isLooping=true
            val alarmLayout=LayoutInflater.from(this@MyService).inflate(R.layout.fragment_alarm,null)
            val messageHolder=alarmLayout.findViewById<TextView>(R.id.textViewMssg)
            messageHolder.text=msg
            val btnClose=alarmLayout.findViewById<Button>(R.id.button_close)
            btnClose.setOnClickListener{
                player.stop()
                alarmLayout.visibility=View.GONE
            }
            val windowManager=this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val layoutParams=WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
                                                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                                                        else WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT
            )
            layoutParams.gravity=Gravity.TOP xor Gravity.CENTER

            windowManager.addView(alarmLayout,layoutParams)
            player.start()
            alarmLayout.visibility=View.VISIBLE
        } else {
            createNotificationChannel()
            val remoteViewsHead = RemoteViews(this@MyService.packageName, R.layout.notification_layout_head)
            val remoteViewsContent = RemoteViews(this@MyService.packageName, R.layout.notification_layout_content)
            remoteViewsContent.setTextViewText(R.id.textViewMess, msg)
            val builder = NotificationCompat.Builder(this@MyService, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomHeadsUpContentView(remoteViewsHead)
                .setCustomBigContentView(remoteViewsContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .build()
            NotificationManagerCompat.from(this@MyService).notify(notificationId, builder)
            val player = MediaPlayer.create(this@MyService, R.raw.notification)
            player.start()
        }
    }
}