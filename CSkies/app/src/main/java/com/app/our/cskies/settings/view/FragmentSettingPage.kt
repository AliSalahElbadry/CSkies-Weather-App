package com.app.our.cskies.settings.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.our.cskies.LocationGetter.FragmentLocationDetector
import com.app.our.cskies.R
import com.app.our.cskies.databinding.FragmentSettingPageBinding
import com.app.our.cskies.shard_pref.SharedPrefOps
import com.app.our.cskies.utils.*

class FragmentSettingPage : Fragment() {

    var isLocationMapSet: Boolean=false
    lateinit var binding:FragmentSettingPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding=FragmentSettingPageBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSettingsUi()
        if(isLocationMapSet)
        {
            Dialogs.SnakeToast(view,if(Setting.getLang()=="en")
                                       "Done Save New Location"
                                        else
                                       "تم حفظ موقعك الجديد"
            )
            val pref= SharedPrefOps(requireActivity().applicationContext)
            pref.insertInData()
            pref.saveLastLocation()
        }
        binding.radioGroupLocation.setOnCheckedChangeListener { group, checkedId ->
          if(UserStates.checkConnectionState(requireActivity())) {
              if (checkedId == R.id.radioButtonGps) {
                  Dialogs.SnakeToast(
                      binding.root,
                      if (Setting.getLang() == "en") "Loading Your Location" else "جاري تحديد موقعك"
                  )
                  Setting.location = Setting.Location.GPS
                  val locationDetector = FragmentLocationDetector()
                  locationDetector.isSetting = true
                  locationDetector.mode = 0
                  requireActivity().supportFragmentManager.beginTransaction()
                      .replace(R.id.my_host_fragment, locationDetector, null)
                      .addToBackStack(null)
                      .commit()
              } else {
                  Setting.location = Setting.Location.MAP
                  val locationDetector = FragmentLocationDetector()
                  locationDetector.isSetting = true
                  locationDetector.mode = 1
                  requireActivity().supportFragmentManager.beginTransaction()
                      .replace(R.id.my_host_fragment, locationDetector, null)
                      .addToBackStack(null)
                      .commit()
              }
          }else{
              Dialogs.SnakeToast(view,if(Setting.getLang()=="en")
                  "Error Connection"
              else
                  "لا يوجد اتصال انترنت"
              )
          }

        }
        binding.radioGroupTemp.setOnCheckedChangeListener { group, checkedId ->
            binding.buttonApplyChange.visibility=View.VISIBLE
        }
        binding.radioGroupNotif.setOnCheckedChangeListener { group, checkedId ->
            binding.buttonApplyChange.visibility=View.VISIBLE

        }
        binding.radioGroupWindSpeed.setOnCheckedChangeListener { group, checkedId ->
            binding.buttonApplyChange.visibility=View.VISIBLE
        }
        binding.radioGroupLang.setOnCheckedChangeListener { group, checkedId ->
            binding.buttonApplyChange.visibility=View.VISIBLE
        }
        binding.buttonApplyChange.setOnClickListener{
            when(binding.radioGroupTemp.checkedRadioButtonId){
                R.id.radioButtonTempC->{
                    Setting.temperature=Setting.Temperature.C
                }
                R.id.radioButtonTempF->{
                    Setting.temperature=Setting.Temperature.F
                }
                else->{
                    Setting.temperature=Setting.Temperature.K
                }
            }
            if(binding.radioGroupNotif.checkedRadioButtonId==R.id.radioButtonNotifOn) {
                Setting.notificationState=Setting.NotificationState.ON
            }else{
                Setting.notificationState=Setting.NotificationState.OFF
            }
            if(binding.radioGroupWindSpeed.checkedRadioButtonId==R.id.radioButtonWSpeedMS) {
                Setting.wSpeed=Setting.WSpeed.M_S
            }else{
                Setting.wSpeed=Setting.WSpeed.MILE_HOUR
            }
            if(binding.radioGroupLang.checkedRadioButtonId==R.id.radioButtonArabic)
            {
                Setting.lang=Setting.Lang.AR
            }else{
                Setting.lang=Setting.Lang.EN
            }
            val pref= SharedPrefOps(requireActivity().applicationContext)
            pref.insertInData()
            pref.saveLastLocation()
            pref.saveAsNewSetting(1)
            LanguageUtils.setAppLocale(Setting.getLang(),requireActivity().applicationContext)
            LanguageUtils.setAppLayoutDirections(Setting.getLang(),requireContext().applicationContext)
            LanguageUtils.changeLang(requireActivity().applicationContext,Setting.getLang())
            requireActivity().finish()
            requireActivity().overridePendingTransition(0, 0)
            startActivity(requireActivity().intent)
            requireActivity().overridePendingTransition(0, 0)
        }
    }
    private fun initSettingsUi() {
          requireActivity().title=resources.getString(R.string.setting)
        binding.buttonApplyChange.visibility=View.INVISIBLE
        if(Setting.getLang()=="en")
        {
            binding.radioGroupLang.check(R.id.radioButtonEnglish)
        }else{
            binding.radioGroupLang.check(R.id.radioButtonArabic)
        }
        if(Setting.notificationState==Setting.NotificationState.OFF)
        {
            binding.radioGroupNotif.check(R.id.radioButtonNotficationOff)
        }else{
            binding.radioGroupNotif.check(R.id.radioButtonNotifOn)
        }
        if(Setting.wSpeed==Setting.WSpeed.MILE_HOUR)
        {
            binding.radioGroupWindSpeed.check(R.id.radioButtonWSpeedMileS)
        }else{
            binding.radioGroupWindSpeed.check(R.id.radioButtonWSpeedMS)
        }
        when(Setting.temperature)
        {
            Setting.Temperature.F->{
                binding.radioGroupTemp.check(R.id.radioButtonTempF)
            }
            Setting.Temperature.K->{
                binding.radioGroupTemp.check(R.id.radioButtonTempK)
            }
            else->{
                binding.radioGroupTemp.check(R.id.radioButtonTempC)
            }
        }
    }
}