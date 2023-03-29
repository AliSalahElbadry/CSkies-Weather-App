package com.app.our.cskies.alerts.alart_back_workers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.app.our.cskies.R
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.alerts.utils.AlarmUtils
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.network.ApiState
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserCurrentLocation
import com.app.our.cskies.utils.UserStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertReceiver : BroadcastReceiver() {
    lateinit var mContext: Context
    private val notificationId: Int=1997169
    private var CHANNEL_ID: String="channel_1234_Skies"
    override fun onReceive(context: Context, intent: Intent) {
        mContext=context
        val alert= intent.getSerializableExtra("alarm")as Alert
        val repo= Repository.getInstance(mContext)
        if(UserStates.checkConnectionState(mContext))
        {
            CoroutineScope(Dispatchers.IO).launch {
                repo.getWeatherData(alert.lat,alert.lon).collect{ apiState ->
                    when(apiState){
                        is ApiState.Success->{
                            if(apiState.locationData.alerts!=null)
                            {
                                val data=apiState.locationData
                                var msg=""
                                msg+="Latitude: ${data.lat}\nLongitude: ${data.lon}"
                                UserCurrentLocation.longitude=data.lon.toString()
                                UserCurrentLocation.latitude=data.lat.toString()
                                msg+="\nAddress: ${UserCurrentLocation.getAddress(mContext,false)}"
                                msg+="\nAlerts: \n"
                                data.alerts.forEach{
                                    msg+="Event: $it.event :\t"
                                    msg+="Description: ${it.description}\n"
                                }
                                withContext(Dispatchers.Main){showAlarmOrNotification(msg,alert.type)}

                            }else{
                                withContext(Dispatchers.Main){ showAlarmOrNotification(if(Setting.getLang()=="en")
                                    "There Is No Weather Alerts In ${alert.address} for Today"
                                else
                                    "لا يوجد تنبيهات طقس  ${alert.address}اليوم "
                                    ,alert.type)
                                }
                            }
                        }
                        is ApiState.Failure->{

                        }
                        else->{

                        }
                    }
                }
            }
        }
        if(alert.numOfDays>0)
        {
            alert.numOfDays-=1
            CoroutineScope(Dispatchers.IO).launch {
                repo.updateAlert(alert)
            }
            AlarmUtils.setAlarm(context.applicationContext,alert)
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                repo.deleteAlert(alert.getId()!!)
            }
            //AlarmUtils.canelAlarm(applicationContext,alert)
        }
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = mContext.getString(R.string.app_name)
            val descriptionText = mContext.getString(R.string.alarm)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    @SuppressLint("MissingPermission")
    private fun showAlarmOrNotification(msg: String, type: Int) {
       if(type==0)
       {
           Log.e("","Fire ALARM 000000000000000000000000000000000000000000")
       }else {
           Log.e("","Fire Notification 111111111111111111111111111111111111111111")
       }
    }
}