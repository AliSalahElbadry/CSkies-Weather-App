package com.app.our.cskies.weather_data_show.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.our.cskies.databinding.HourItemBinding
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.utils.Constants
import com.app.our.cskies.utils.Converter
import com.app.our.cskies.utils.LanguageUtils
import com.app.our.cskies.utils.Setting
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


class HoursAdapter(var hours:List<HourWeather>) : RecyclerView.Adapter<HoursAdapter.MyHolder>() {

    lateinit var binding:HourItemBinding
    class MyHolder(var binding:HourItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        binding= HourItemBinding.inflate(parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater,parent,false)
        LanguageUtils.setAppLocale(Setting.getLang(),binding.root.context)
        return MyHolder(binding)
    }
    override fun getItemCount(): Int {
        return hours.size
    }
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.textViewHourItemHour.text=hours[position].hour
        //converter
         holder.binding.textViewTempHourItem.text=
            if(Setting.getLang()=="ar")
            "${LanguageUtils.get_En_To_Ar_Numbers(Converter.getTemperature(hours[position].temp).toString())}${Constants.DEGREE_CHAR}${Setting.getTemp()}"
        else
            "${Converter.getTemperature(hours[position].temp)}${Constants.DEGREE_CHAR}${Setting.getTemp()}"

         holder.binding.imageViewIconHourItemHour.setImageBitmap(hours[holder.adapterPosition].getImageBitmap())
    }
}