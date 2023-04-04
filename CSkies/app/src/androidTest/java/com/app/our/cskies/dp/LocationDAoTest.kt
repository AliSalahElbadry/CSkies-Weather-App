package com.app.our.cskies.dp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.our.cskies.MainRule
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.getOrAwaitValue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class LocationDAoTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainRule = MainRule()
    private lateinit var database:AppDataBase
    private lateinit var locationDao:LocationDAO
    @Before
    fun setUpDatabase(){
        database= Room.inMemoryDatabaseBuilder (
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
            ).build()
        locationDao=database.getLocationDao()
    }
    @After
    fun closeDatabase(){
        database.close()
    }

    @Test
    fun insertLocation_CheckAsCurrentLocation()=mainRule.runBlockingTest{
        //Given
        val  location = Location(
        "en","5/4/2023",
        "egypt","clear sky",
        true,15,10,
        2,12,"",true,1)

        //When
        locationDao.insertLocation(location)

        //Then
        val res=locationDao.getCurrentLocation(true, location.lang).asLiveData().getOrAwaitValue {  }
        assertThat(res,`is`(notNullValue()))
        assertThat(res.isCurrent,`is`(true))
        assertThat(res.address,`is`("egypt"))
        assertThat(res.description,`is`("clear sky"))
    }

    @Test
    fun insertDay_checkInserted()=mainRule.runBlockingTest{
        //Given
        val day=DayWeather("Sunday","egypt",50,30,"clear sky","")

        //When
        locationDao.insertDay(day)

        //Then
        val res=locationDao.selectDaysOfLocation(day.address).asLiveData().getOrAwaitValue {  }
        assertThat(res,`is`(notNullValue()))
        assertThat(res[0].address,`is`("egypt"))
        assertThat(res[0].description,`is`("clear sky"))
    }

    @Test
    fun insertHour_checkInserted()=mainRule.runBlockingTest{
       //Given
        val hour=HourWeather("Sunday","5:20",50,"")

        //When
        locationDao.insertHour(hour)

        //Then
        val res=locationDao.selectHoursInLocation(hour.address).asLiveData().getOrAwaitValue {  }
        assertThat(res,`is`(notNullValue()))
        assertThat(res[0].address,`is`(hour.address))
        assertThat(res[0].hour,`is`(hour.hour))
    }

    @Test
    fun insertAlert_checkInserted()=mainRule.runBlockingTest{
        //Given
        val alert=Alert(
            "23345656",
            "36565555",
            true,
            true,
            true,
            true,
            true,
            "egypt",
            "31.02211",
            "31.00512",
            1
        )

        //When
        locationDao.insertAlert(alert)

        //Then
        val res=locationDao.getAlert(alert.address,alert.type)
        assertThat(res,`is`(notNullValue()))
        assertThat(res.address,`is`(alert.address))
        assertThat(res.fromDate,`is`(alert.fromDate))
    }
    //delete
    @Test
    fun deleteLocation_checkDeleted()=mainRule.runBlockingTest{
        //Given
        val  location = Location(
            "en","5/4/2023",
            "egypt","clear sky",
            true,15,10,
            2,12,"",true,1)
        locationDao.insertLocation(location)
        //When
        locationDao.deleteLocation(location.address)

        //Then
        val res=locationDao.getCurrentLocation(true, location.lang).asLiveData().getOrAwaitValue {  }
        assertThat(res,`is`(nullValue()))
    }

    @Test
    fun deleteDay_checkDeleted()=mainRule.runBlockingTest{
        //Given
        val day=DayWeather("Sunday","egypt",50,30,"clear sky","")
        locationDao.insertDay(day)
        //When
        locationDao.deleteDay(day.address)
        //Then
        val res=locationDao.selectDaysOfLocation(day.address).asLiveData().getOrAwaitValue {  }
        assertThat(res.size,`is`(0))
    }

    @Test
    fun deleteHour_checkDeleted()=mainRule.runBlockingTest {
        //Given
        val hour = HourWeather("Sunday", "5:20", 50, "")
        locationDao.insertHour(hour)
        //When
        locationDao.deleteHour(hour.address)
        //Then
        val res = locationDao.selectHoursInLocation(hour.address).asLiveData().getOrAwaitValue { }
        assertThat(res.size, `is`(0))
    }
    @Test
    fun deleteAlert_checkDeleted()=mainRule.runBlockingTest{
        //Given
        val alert=Alert(
            "23345656",
            "36565555",
            true,
            true,
            true,
            true,
            true,
            "egypt",
            "31.02211",
            "31.00512",
            1
        )
        alert.setId(0)
        locationDao.insertAlert(alert)

        //When
        locationDao.deleteAlert(alert.getId()!!)
        //Then
        val res=locationDao.getAlert(alert.address,alert.type)
        assertThat(res,`is`(nullValue()))
    }


    //select

    @Test
    fun getListOfFavLocations_returnListOfFavorites()=mainRule.runBlockingTest{
        //Given
        val  location = Location(
            "en","5/4/2023",
            "egypt","clear sky",
            true,15,10,
            2,12,"",false,1)
        locationDao.insertLocation(location)
        location.address="china"
        locationDao.insertLocation(location)
        location.address="algeria"
        locationDao.insertLocation(location)
        //When
        val res=locationDao.getListOfFavLocations(true).asLiveData().getOrAwaitValue {  }

        //Then
        assertThat(res,`is`(notNullValue()))
        assertThat(res[0].isFavorite,`is`(true))
        assertThat(res[1].isFavorite,`is`(true))
        assertThat(res[2].isFavorite,`is`(true))
    }

    @Test
     fun updateAlert_DecreaseNumberOfDays_checkUpdated()=mainRule.runBlockingTest{
        //Given
        val alert=Alert(
            "23345656",
            "36565555",
            true,
            true,
            true,
            true,
            true,
            "egypt",
            "31.02211",
            "31.00512",
            1
        )
        alert.setId(0)
        locationDao.insertAlert(alert)
        //When
        locationDao.updateAlert(alert.numOfDays-1,alert.getId()!!)
        //Then
        val res=locationDao.getListOfAlerts().asLiveData().getOrAwaitValue{}
        assertThat(res,`is`(notNullValue()))
        assertThat(res.size,`is`(1))
        assertThat(res[0].numOfDays,`is`(0))
    }


}