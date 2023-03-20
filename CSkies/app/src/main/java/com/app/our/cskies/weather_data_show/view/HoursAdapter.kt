package com.app.our.cskies.weather_data_show.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.our.cskies.databinding.HourItemBinding
import com.app.our.cskies.utils.Constants
import com.app.our.cskies.network.model.Hourly
import com.bumptech.glide.Glide

class HoursAdapter(var hours:List<Hourly>) : RecyclerView.Adapter<HoursAdapter.MyHolder>() {

    lateinit var binding:HourItemBinding
    class MyHolder(var binding:HourItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        binding= HourItemBinding.inflate(parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater,parent,false)
        return MyHolder(binding)
    }
    override fun getItemCount(): Int {
        return hours.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.textViewHourItemHour.text=hours[position].dt.toString()
        //converter
        holder.binding.textViewTempHourItem.text=hours[position].temp.toInt().toString()
        Glide.with(holder.binding.cardViewHourItem).load(Constants.IMG_URL+hours[position].weather[0].icon+".png").into(holder.binding.imageViewIconHourItemHour)
    }
}