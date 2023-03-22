package com.app.our.cskies

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.our.cskies.dp.AppDataBase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//@ExprimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class LocationDAoTest {

    lateinit var database:AppDataBase
    @Before
    fun setUpDatabase(){
   //     database=AppDataBase.getInstance(getApplicationContext())
    }
    @After
    fun closeDatabase(){
        database.close()
    }
    @Test
    fun getListOfFavLocations_insertAndRetrive_returnListOfLocations()
    {

    }
}