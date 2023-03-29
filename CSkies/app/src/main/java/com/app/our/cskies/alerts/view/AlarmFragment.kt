package com.app.our.cskies.alerts.view

import android.app.Dialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import com.app.our.cskies.R
import com.app.our.cskies.databinding.FragmentAlarmaBinding

class AlarmFragment(val mcontext:Context,val msg:String) : Dialog(mcontext) {
    lateinit var player: MediaPlayer
    lateinit var binding: FragmentAlarmaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=FragmentAlarmaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        player = MediaPlayer.create(mcontext, R.raw.alarm)
        player.isLooping=true
        player.start()
        binding.textViewMessage.text=msg
        binding.buttonClose.setOnClickListener{
            player.stop()
            dismiss()
        }
    }

}