package com.app.our.cskies.alerts.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Point
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.app.our.cskies.R
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.alerts.viewmodel.AlertViewModelFactory
import com.app.our.cskies.alerts.viewmodel.AlertsViewModel
import com.app.our.cskies.databinding.FragmentAlertToFromDatesBinding
import com.app.our.cskies.utils.Dialogs
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.UserCurrentLocation
import java.util.*

class FragmentAlertToFromDates : DialogFragment() {
    lateinit var binding:FragmentAlertToFromDatesBinding
    lateinit var viewModel: AlertsViewModel

    var dateTo=Calendar.getInstance(Locale("en"))
    var dateFrom=Calendar.getInstance(Locale("en"))
    var isSetFrom=false
    var isSetTo=false
    var numOfDaysTo:Long=0
    var numOfDayFrom:Long=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding=FragmentAlertToFromDatesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(R.drawable.cat_bcak_item)
        super.onResume()


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.radioGroupAlertType.check(R.id.radioButtonAlarmAlert)
        binding.buttonFromDate.setOnClickListener{
            val dateDialog=DatePickerDialog(requireContext())
            dateDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                numOfDayFrom=(year*365+month*30+dayOfMonth).toLong()
                dateFrom.set(Calendar.YEAR,year)
                dateFrom.set(Calendar.MONTH,month)
                dateFrom.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                isSetFrom=true
               showTimePicker(0)
            }
            dateDialog.datePicker.minDate=Calendar.getInstance().timeInMillis
            dateDialog.show()
        }
        binding.buttonToDate.setOnClickListener{
            val dateDialog=DatePickerDialog(requireContext())
            dateDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                numOfDaysTo=(year*365+month*30+dayOfMonth).toLong()
                dateTo.set(Calendar.YEAR,year)
                dateTo.set(Calendar.MONTH,month)
                dateTo.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                showTimePicker(1)
            }
            dateDialog.datePicker.minDate=Calendar.getInstance().timeInMillis
            dateDialog.show()
        }
        binding.buttonAddAlert.setOnClickListener{
            Log.e("","Added............date")
            if(isSetFrom&&isSetTo&&(numOfDaysTo-numOfDayFrom)>=0&&(dateTo.get(Calendar.HOUR_OF_DAY)-dateFrom.get(Calendar.HOUR_OF_DAY))>=0)
            {
                viewModel.setAlertToFrom(
                    dateTo.timeInMillis.toString(),
                    dateFrom.timeInMillis.toString(),
                    if(binding.radioGroupAlertType.checkedRadioButtonId==R.id.radioButtonAlarmAlert)0 else 1,
                    UserCurrentLocation.getAddress(requireContext(),true),(numOfDaysTo-numOfDayFrom).toInt())
                Dialogs.SnakeToast(binding.root,if(Setting.getLang()=="en") "Done Adding Alert" else "تم اضافة التنبيه")
                dismiss()
            }else{
                Dialogs.SnakeToast(binding.root,if(Setting.getLang()=="en") "Please Select All Parts" else "من فضلك اكمل اختيار الوقت")
            }
        }
    }
    private fun showTimePicker(mode:Int){
        TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
            if(mode==0) {
                dateFrom.set(Calendar.HOUR_OF_DAY,hourOfDay)
                dateFrom.set(Calendar.MINUTE,minute)
                binding.textViewDateFrom.text=SimpleDateFormat("yyyy-mm-dd hh:mm",Locale(Setting.getLang())).format(dateFrom.timeInMillis)
            }
            else {
                isSetTo=true
                dateTo.set(Calendar.HOUR_OF_DAY,hourOfDay)
                dateTo.set(Calendar.MINUTE,minute)
                binding.textViewDateTo.text=SimpleDateFormat("yyyy-mm-dd hh:mm",Locale(Setting.getLang())).format(dateTo.timeInMillis)
            }
        },24,60,false).show()
    }
}