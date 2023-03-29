package com.app.our.cskies.home.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ViewModelHome :ViewModel() {

    private var _title= MutableStateFlow<Int>(0)
    val title:StateFlow<Int> =_title
    fun setTitle(screen:Int){
       _title.value=screen
   }
}