package com.app.our.cskies.weather_data_show.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.our.cskies.databinding.DayItemBinding
import com.app.our.cskies.utils.Constants
import com.app.our.cskies.network.model.Daily
import com.bumptech.glide.Glide

class DaysAdapter (var daily:List<Daily>) : RecyclerView.Adapter<DaysAdapter.MyHolder>() {

    private lateinit var binding:DayItemBinding
    class MyHolder(var binding: DayItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        binding= DayItemBinding.inflate(parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater,parent,false)
        return MyHolder(binding)
    }
    override fun getItemCount(): Int {
        return daily.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.textViewDescriptionDayItem.text=daily[position].weather[0].description
        //converter
        holder.binding.textViewNameDayItem.text=daily[position].dt.toString()
        holder.binding.textViewTempretureDayItem.text=daily[position].temp.min.toInt().toString()+"/"+daily[position].temp.max.toInt().toString()
        //end
        Glide.with(holder.binding.cardViewDayItem).load(Constants.IMG_URL+daily[position].weather[0].icon+".png").into(holder.binding.imageViewDayWeatherIcon)
    }
}