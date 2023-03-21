package com.app.our.cskies.utils

import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TimeUtilsTest {

    @Test
    fun formatDayTime_longDate_returnHHMM() {
        //Getting
        val dt: Long=1679303520
        //When
        val cat=TimeUtils.formatDayTime(dt)
        //Then
       assertEquals("11:12",cat)
    }
    @Test
    fun formatTimeToArabic_LongDate_returnHHMM() {
        //Getting
        val dt: Long=1679303520
        //When
        val res=TimeUtils.formatTimeToArabic(dt)
        //Then
        assertEquals("١١:١٢",res)
    }

    @Test
    fun getDateFormat_returnDDMMYYYY() {
        //Getting
        val dt: Long=1679303520
        //When
        val res=TimeUtils.getDateFormat(dt)
        //Then
        assertEquals("20-03-2023",res)
    }

    @Test
    fun formatDateAlert_returnDDMMYYYY() {
        //Getting
        val dt: Long=1679303520
        //When
        val res=TimeUtils.formatDateAlert(dt)
        //Then
        assertEquals("20-01-1970",res)
    }

    @Test
    fun formatTimeAlert_returnHHMM() {
        //Getting
        val dt: Long=1679303520
        //When
        val res=TimeUtils.formatTimeAlert(dt)
        //Then
        assertEquals("12:28",res)
    }

    @Test
    fun getDateArabic_returnDDMMYYYY() {
        //Getting
        val dt: Long=1679303520
        //When
        val res=TimeUtils.getDateArabic(dt)
        //Then
        assertEquals("٢٠-٠٣-٢٠٢٣",res)
    }

    @Test
    fun getDayFormat_returnDayName() {
        //Getting
        val dt: Long=1679303520
        //When
        val res=TimeUtils.getDayFormat(dt)
        //Then
        assertEquals("Monday",res)
    }

    @Test
    fun getDayArabic_returnDayNameArabic() {
        //Getting
        val dt: Long=1679303520
        //When
        val res=TimeUtils.getDayArabic(dt)
        //Then
        assertEquals("الاثنين",res)
    }
}