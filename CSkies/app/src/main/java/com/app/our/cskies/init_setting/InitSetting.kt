package com.app.our.cskies.init_setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.app.our.cskies.LocationGetter.FragmentLocationDetector
import com.app.our.cskies.R
import com.app.our.cskies.databinding.FragmentInitSettingsBinding
import com.app.our.cskies.utils.Dialogs
import com.app.our.cskies.utils.Setting


class InitSetting : DialogFragment() {
    lateinit var binding: FragmentInitSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding=FragmentInitSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initSettingDone.setOnClickListener{
           if(binding.radioGroupLoactionInit.checkedRadioButtonId!=-1&&binding.radioGroupNotifInit.checkedRadioButtonId!=-1){
               if(binding.radioGroupLoactionInit.checkedRadioButtonId==R.id.radioButtonGpsInit)
               {
                   Setting.location= Setting.Location.GPS

               }else if(binding.radioGroupLoactionInit.checkedRadioButtonId==R.id.radioButtonMapInit)
               {
                   Setting.location= Setting.Location.MAP
               }
               if(binding.radioGroupNotifInit.checkedRadioButtonId==R.id.radioButtonNotifOnInit)
               {
                   Setting.notificationState= Setting.NotificationState.ON
               }else if(binding.radioGroupNotifInit.checkedRadioButtonId==R.id.radioButtonNotifOffInit)
               {
                   Setting.notificationState= Setting.NotificationState.OFF
               }
               val locationFragment = FragmentLocationDetector()
               locationFragment.isCancelable = false
               val manager = requireActivity().supportFragmentManager
               locationFragment.show(manager,null)
               dismiss()
           }else{
               Dialogs.SnakeToast(it,"Please Select All Required Data")
           }
        }
    }

    override fun onResume() {
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(R.drawable.cat_bcak_item)
        super.onResume()
    }
}