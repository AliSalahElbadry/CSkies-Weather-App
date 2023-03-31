package com.app.our.cskies.alerts.alart_back_workers

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.our.cskies.R
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.alerts.utils.AlarmUtils
import com.app.our.cskies.dp.LocalDataSource
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
                repo.getWeatherData(alert.lat, alert.lon).collect { apiState ->
                    when (apiState) {
                        is ApiState.Success -> {
                            if (apiState.locationData.alerts != null) {
                                val data = apiState.locationData
                                var msg = ""
                                msg += "Latitude: ${data.lat}\nLongitude: ${data.lon}"
                                UserCurrentLocation.longitude = data.lon.toString()
                                UserCurrentLocation.latitude = data.lat.toString()
                                msg += "\nAddress: ${
                                    UserCurrentLocation.getAddress(
                                        this@MyService,
                                        false
                                    )
                                }"
                                msg += "\nAlerts: \n"
                                data.alerts.forEach {
                                    msg += "Event: $it.event :\t"
                                    msg += "Description: ${it.description}\n"
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
                                            "There Is No Weather Alerts In ${alert.address} for Today"
                                        else
                                            "لا يوجد تنبيهات طقس  ${alert.address}اليوم ",
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
        }
        if (alert.numOfDays > 0) {
            alert.numOfDays -= 1
            CoroutineScope(Dispatchers.IO).launch {
                repo.updateAlert(alert)
            }
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
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
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
            createNotificationChannel()
            CoroutineScope(Dispatchers.Default).launch {
                createNotificationChannel()
                val intent=Intent(this@MyService,CloseHundleReciever::class.java).putExtra("NOTIFICATION_ID",notificationId)
                val pendingIntent=PendingIntent.getBroadcast(this@MyService,0,intent,FLAG_IMMUTABLE)
                val remoteViews = RemoteViews(this@MyService.packageName, R.layout.fragment_alarm)
                remoteViews.setTextViewText(R.id.textViewMssg, msg)
                remoteViews.setOnClickPendingIntent(R.id.textViewMssg,pendingIntent)
                //var action=NotificationCompat.Action()

                val builder = NotificationCompat.Builder(this@MyService, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setCustomContentView(remoteViews)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(Notification.CATEGORY_ALARM)
                    .setSound(Uri.parse("android.resource://" + this@MyService.packageName + "/" + R.raw.alarm))
                    //.addAction(0,"action",pendingIntent)
                    .setStyle(NotificationCompat.InboxStyle())
                    .build()
                 //builder.flags.xor(Notification.FLAG_INSISTENT)
                NotificationManagerCompat.from(this@MyService).notify(notificationId, builder)
            }
        } else {
            createNotificationChannel()
            val remoteViews = RemoteViews(this@MyService.packageName, R.layout.fragment_alarma)
            remoteViews.setTextViewText(R.id.textViewMess, msg)
            val builder = NotificationCompat.Builder(this@MyService, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(remoteViews)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_ALARM)
                .setAutoCancel(true)
                .build()
            NotificationManagerCompat.from(this@MyService).notify(notificationId, builder)
            val player = MediaPlayer.create(this@MyService, R.raw.notification)
            player.start()
        }
    }
}