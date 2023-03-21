package com.app.our.cskies.favorites.view

import com.app.our.cskies.dp.model.Location

interface IOnClickItemListener {
    fun onClick(location: Location)
    fun onClickDelete(location: Location)
}