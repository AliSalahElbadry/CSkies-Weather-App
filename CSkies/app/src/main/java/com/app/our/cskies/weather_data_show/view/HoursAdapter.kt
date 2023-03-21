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
import com.app.our.cskies.utils.Converter
import com.app.our.cskies.utils.LanguageUtils
import com.app.our.cskies.utils.Setting
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


class HoursAdapter(var hours:List<HourWeather>,val container:NumloadedImages) : RecyclerView.Adapter<HoursAdapter.MyHolder>() {

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
        holder.binding.textViewTempHourItem.text=Converter.getTemperature(hours[position].temp).toString()+" "+Setting.getTemp()
       if(container.getMyMode()==0) {
           if(hours[holder.adapterPosition].getImageBitmap()==null) {
               Glide.with(holder.itemView.context)
                   .asBitmap()
                   .load(hours[position].icon)
                   .into(object : CustomTarget<Bitmap>() {
                       override fun onResourceReady(
                           resource: Bitmap,
                           transition: Transition<in Bitmap>?
                       ) {
                           container.setNumLoadedImages(1)
                           hours[holder.adapterPosition].setImageBitmap(resource)
                           holder.binding.imageViewIconHourItemHour.setImageBitmap(resource)
                       }

                       override fun onLoadCleared(placeholder: Drawable?) {

                       }
                   })
           }else{
               holder.binding.imageViewIconHourItemHour.setImageBitmap(hours[holder.adapterPosition].getImageBitmap())
           }
       }else{
           holder.binding.imageViewIconHourItemHour.setImageBitmap(hours[holder.adapterPosition].getImageBitmap())
       }
    }
}