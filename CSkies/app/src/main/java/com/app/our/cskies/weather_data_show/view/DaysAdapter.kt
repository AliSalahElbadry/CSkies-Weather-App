package com.app.our.cskies.weather_data_show.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.our.cskies.databinding.DayItemBinding
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.network.model.Daily
import com.app.our.cskies.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class DaysAdapter (var daily:List<DayWeather>,val container:NumloadedImages) : RecyclerView.Adapter<DaysAdapter.MyHolder>() {
    private lateinit var binding:DayItemBinding
    class MyHolder(var binding: DayItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        binding= DayItemBinding.inflate(parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater,parent,false)
        LanguageUtils.setAppLocale(Setting.getLang(),binding.root.context)
        return MyHolder(binding)
    }
    override fun getItemCount(): Int {
        return daily.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.textViewDescriptionDayItem.text=daily[position].description
        //converter
        holder.binding.textViewNameDayItem.text=daily[position].day
        holder.binding.textViewTempretureDayItem.text="${Converter.getTemperature(daily[position].minTemp)}/" +
                                                      "${Converter.getTemperature(daily[position].maxTemp)} ${Setting.getTemp()}"
        if(container.getMyMode()==0) {
            if(daily[holder.adapterPosition].getImageBitmap()==null) {
                Glide.with(holder.itemView.context)
                    .asBitmap()
                    .load(daily[position].icon)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            container.setNumLoadedImages(1)
                            daily[holder.adapterPosition].setImageBitmap(resource)
                            holder.binding.imageViewDayWeatherIcon.setImageBitmap(resource)
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            }else{
                holder.binding.imageViewDayWeatherIcon.setImageBitmap(daily[position].getImageBitmap())
            }
        }
        else if (container.getMyMode()==1||container.getMyMode()==2)
        {
            holder.binding.imageViewDayWeatherIcon.setImageBitmap(daily[position].getImageBitmap())
        }
    }
}