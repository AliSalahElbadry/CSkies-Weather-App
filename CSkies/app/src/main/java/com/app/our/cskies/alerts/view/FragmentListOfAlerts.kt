package com.app.our.cskies.alerts.view

import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.app.our.cskies.R
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.alerts.viewmodel.AlertViewModelFactory
import com.app.our.cskies.alerts.viewmodel.AlertsViewModel
import com.app.our.cskies.databinding.AlertItemBinding
import com.app.our.cskies.databinding.FragmentListOfAlertsBinding
import com.app.our.cskies.utils.Dialogs
import com.app.our.cskies.utils.Setting

class FragmentListOfAlerts : DialogFragment() {
 lateinit var binding: FragmentListOfAlertsBinding
 lateinit var viewModel: AlertsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentListOfAlertsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonDoneAlert.setOnClickListener{
            if(!binding.checkBoxtemp.isChecked&&
            !binding.checkBoxWS.isChecked&&
            !binding.checkBoxhum.isChecked&&
            !binding.checkBoxVis.isChecked&&
            !binding.checkBoxPress.isChecked){
                Dialogs.SnakeToast(binding.root,
                if(Setting.getLang()=="en")"Please Check at least one" else "من فضلك اختر اخيار واحد علي الاقل")
            }else {
                viewModel.setAlertTypes(
                    binding.checkBoxtemp.isChecked,
                    binding.checkBoxWS.isChecked,
                    binding.checkBoxhum.isChecked,
                    binding.checkBoxVis.isChecked,
                    binding.checkBoxPress.isChecked
                )
                val fragmentAlertToFromDates = FragmentAlertToFromDates()
                fragmentAlertToFromDates.isCancelable = false
                fragmentAlertToFromDates.viewModel=viewModel
                fragmentAlertToFromDates.show(requireActivity().supportFragmentManager, null)
                dismiss()
            }
        }
    }

    override fun onResume() {
        val window = dialog!!.window
        val size = Point()
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        window.setLayout((size.x * 0.8).toInt(), (size.y*0.5).toInt())
        window.setBackgroundDrawableResource(R.drawable.cat_bcak_item)
        super.onResume()
    }
}