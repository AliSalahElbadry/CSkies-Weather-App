package com.app.our.cskies.utils

import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.text.SimpleDateFormat
import java.util.*

@RunWith(JUnit4::class)
class TimeUtilsTest {
    @Test
    fun formatDayTime_convertLongDayTimeToString_returnTimeAs_hh_mm_aa(){
        //Given
        val dt=	1680546427L
        //When
        val res=TimeUtils.formatDayTime(dt)
        //Then
        assertEquals(res,"08:27 PM")
    }
    @Test
    fun getDate_convertLongDateToString_returnDataAs_dd_mm_yyyy(){
        //Given
        val dt=	1680546427L
        //When
        val res=TimeUtils.getDate(dt)
        //Then
        assertEquals(res,"03-04-2023")
    }
    @Test
    fun getDay_ConvertLongToDay_returnDayName(){
        //Given
        val dt=	1680546427L
        //When
        val res=TimeUtils.getDay(dt)
        //Then
        assertEquals(res,"Monday")
    }
    @Test
    fun getHour_ConvertLongToHour_returnHour(){
        //Given
        val dt=	1680546427L
        //When
        val res=TimeUtils.getHour(dt)
        //Then
        assertEquals(res,"08 PM")
    }
}