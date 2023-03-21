package com.app.our.cskies.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.our.cskies.Repository.Repository
import com.app.our.cskies.Repository.RepositoryInterface

class FavoritesViewModelFactory (private val myRepo: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(FavoritesViewModel::class.java)){
            FavoritesViewModel(myRepo) as T
        }else{
            throw IllegalArgumentException()
        }
    }
}