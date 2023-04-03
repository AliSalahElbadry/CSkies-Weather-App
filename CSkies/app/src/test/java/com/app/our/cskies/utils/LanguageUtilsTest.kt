package com.app.our.cskies.utils

import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LanguageUtilsTest {

    @Test
    fun get_En_To_Ar_Numbers() {
        //Given
        var num="1234567890"
        //When
        num=LanguageUtils.get_En_To_Ar_Numbers(num)
        //Then
        assertEquals(num,"١٢٣٤٥٦٧٨٩٠")
    }
}