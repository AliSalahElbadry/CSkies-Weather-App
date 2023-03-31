package com.app.our.cskies.alerts.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.our.cskies.databinding.AlertItemBinding
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.utils.LanguageUtils
import com.app.our.cskies.utils.Setting
import com.app.our.cskies.utils.TimeUtils

class AlertsAdapter (var alerts:List<Alert>,val listener:OnClickDeleteAlert): RecyclerView.Adapter<AlertsAdapter.MyHolder>(){
    private lateinit var binding: AlertItemBinding
    class MyHolder(var binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertsAdapter.MyHolder {
        binding= AlertItemBinding.inflate(parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater,parent,false)
        LanguageUtils.setAppLocale(Setting.getLang(),binding.root.context)
        return AlertsAdapter.MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return alerts.size
    }

    override fun onBindViewHolder(holder: AlertsAdapter.MyHolder, position: Int) {
        holder.binding.imgBtnAlertItem.setOnClickListener{
            listener.onClick(alerts[holder.adapterPosition])
        }
        holder.binding.textViewCityAlertItem.text=alerts[holder.adapterPosition].address
        holder.binding.textViewAlertTimeFrom.text=TimeUtils.getDateTimeAlert(alerts[holder.adapterPosition].fromDate.toLong())
        holder.binding.textViewAlertTimeTo.text=TimeUtils.getDateTimeAlert(alerts[holder.adapterPosition].toDate.toLong())
    }
}