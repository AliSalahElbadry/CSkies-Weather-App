package com.app.our.cskies.favorites.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.app.our.cskies.databinding.FavoriteItemBinding
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.utils.Dialogs
import com.app.our.cskies.utils.LanguageUtils
import com.app.our.cskies.utils.Setting

class FavoritesAdapter  (var locations:MutableList<Location>, private val listener: IOnClickItemListener) : RecyclerView.Adapter<FavoritesAdapter.MyHolder>() {
    private lateinit var binding: FavoriteItemBinding
    class MyHolder(var binding: FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        binding= FavoriteItemBinding.inflate(parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater,parent,false)
        LanguageUtils.setAppLocale(Setting.getLang(),binding.root.context)
        return MyHolder(binding)
    }
    override fun getItemCount(): Int {
        return locations.size
    }
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.textViewCityFavoriteItem.text=locations[position].address
        holder.binding.cardViewFavoriteItem.setOnClickListener {
            listener.onClick(locations[holder.adapterPosition])
            Log.e("","Clicked")
        }
        holder.binding.imgBtnFavoriteItem.setOnClickListener{
           val builder= Dialogs.getAletDialogBuilder(binding.root.context,"Alert !!","Do You Want really to Delete Location ?")
            builder.setPositiveButton("No"){ dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            builder.setNegativeButton("Yes"){ _, _ ->
                listener.onClickDelete(locations[holder.adapterPosition])
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}