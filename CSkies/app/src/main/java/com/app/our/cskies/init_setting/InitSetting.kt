package com.app.our.cskies.init_setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.app.our.cskies.LocationGetter.FragmentLocationDetector
import com.app.our.cskies.R
import com.app.our.cskies.messages.Toasts
import com.app.our.cskies.model.Setting
import com.google.android.material.snackbar.Snackbar


class InitSetting : DialogFragment() {
    lateinit var  radioGroupLocation:RadioGroup
    lateinit var radioGroupNotification: RadioGroup
    lateinit var btnDone:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_init_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioGroupLocation=view.findViewById(R.id.radioGroupLoactionInit)
        radioGroupNotification=view.findViewById(R.id.radioGroupNotifInit)
        btnDone=view.findViewById(R.id.initSettingDone)
        btnDone.setOnClickListener{
           if(radioGroupLocation.checkedRadioButtonId!=-1&&radioGroupNotification.checkedRadioButtonId!=-1){
               if(radioGroupLocation.checkedRadioButtonId==R.id.radioButtonGpsInit)
               {
                   Setting.location=Setting.Location.GPS

               }else if(radioGroupLocation.checkedRadioButtonId==R.id.radioButtonMapInit)
               {
                   Setting.location=Setting.Location.MAP

               }
               if(radioGroupNotification.checkedRadioButtonId==R.id.radioButtonNotifOnInit)
               {
                   Setting.notificationState=Setting.NotificationState.ON
               }else if(radioGroupNotification.checkedRadioButtonId==R.id.radioButtonNotifOffInit)
               {
                   Setting.notificationState=Setting.NotificationState.OFF
               }
               val locationFragment = FragmentLocationDetector()
               locationFragment.isCancelable = false
               val manager = requireActivity().supportFragmentManager
               locationFragment.show(manager,null)
               dismiss()
           }else{
               Toasts.SnakeToast(it,"Please Select All Required Data")
           }
        }
    }

    override fun onResume() {
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(R.drawable.cat_bcak_item)
        super.onResume()
    }
}