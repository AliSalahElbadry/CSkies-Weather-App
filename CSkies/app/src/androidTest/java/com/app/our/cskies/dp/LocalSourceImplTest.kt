package com.app.our.cskies.dp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.app.our.cskies.MainRule
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.getOrAwaitValue
import com.app.our.cskies.model.LocationData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
class LocalSourceImplTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainRule = MainRule()

    lateinit var localSourceImpl: LocalDataSource
    lateinit var database:AppDataBase
    @Before
    fun setUpLocalDataSource(){
        database= Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),AppDataBase::class.java).build()
        localSourceImpl=LocalSourceImpl.getInstance(database.getLocationDao())
    }

    @Test
    fun insertLocation_checkInserted()=mainRule.runBlockingTest {
        //Given
        val locationData=LocationData(location = Location(
            "en","5/4/2023",
            "egypt","clear sky",
            true,15,10,
            2,12,"",true,1),
                mutableListOf<DayWeather>(
            DayWeather("sun day","egypt",45,33,"clear sky",""),
            DayWeather("mon day","egypt",45,33,"clear sky",""),
            DayWeather("teuth day","egypt",45,33,"clear sky",""),
            DayWeather("Wedns day","egypt",45,33,"clear sky",""),
            DayWeather("Thurs day","egypt",45,33,"clear sky",""),
            DayWeather("Fri day","egypt",45,33,"clear sky",""),
            DayWeather("satr day","egypt",45,33,"clear sky","")
        ),
                mutableListOf<HourWeather>(
            HourWeather("egypt","12PM",33,""),
            HourWeather("egypt","1AM",33,""),
            HourWeather("egypt","2AM",33,""),
            HourWeather("egypt","3AM",33,""),
            HourWeather("egypt","4AM",33,""),
            HourWeather("egypt","5AM",33,""),
            HourWeather("egypt","6AM",33,""),
            HourWeather("egypt","7AM",33,""),
            HourWeather("egypt","8AM",33,""),
            HourWeather("egypt","9AM",33,""),
            HourWeather("egypt","10AM",33,""),
            HourWeather("egypt","11AM",33,""),
            HourWeather("egypt","12AM",33,""),
            HourWeather("egypt","1PM",33,""),
            HourWeather("egypt","2PM",33,""),
            HourWeather("egypt","3PM",33,""),
            HourWeather("egypt","4PM",33,""),
            HourWeather("egypt","5PM",33,""),
            HourWeather("egypt","6PM",33,""),
            HourWeather("egypt","7PM",33,""),
            HourWeather("egypt","8PM",33,""),
            HourWeather("egypt","9PM",33,""),
            HourWeather("egypt","10PM",33,""),
            HourWeather("egypt","11PM",33,"")
        ))
        //When
        localSourceImpl.insertLocation(locationData)

        //Then
        val res=localSourceImpl.getCurrentLocation(true).asLiveData().getOrAwaitValue {  }
        assertThat(res,`is`(not(nullValue())))
    }

    @Test
    fun insertAlert_checkInserted()=mainRule.runBlockingTest {
        //Given
        val alert= Alert(
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
        localSourceImpl.insertAlert(alert)

        //Then
        val res=localSourceImpl.getAlert(alert.address,alert.type)
        assertThat(res,`is`(notNullValue()))
        assertThat(res.address,`is`(alert.address))
        assertThat(res.fromDate,`is`(alert.fromDate))
    }

    @Test
    fun deleteLocation_checkDeleted()=mainRule.runBlockingTest {
        //Given
        val locationData=LocationData(location = Location(
            "en","5/4/2023",
            "egypt","clear sky",
            true,15,10,
            2,12,"",true,1),
            mutableListOf<DayWeather>(
                DayWeather("sun day","egypt",45,33,"clear sky",""),
                DayWeather("mon day","egypt",45,33,"clear sky",""),
                DayWeather("teuth day","egypt",45,33,"clear sky",""),
                DayWeather("Wedns day","egypt",45,33,"clear sky",""),
                DayWeather("Thurs day","egypt",45,33,"clear sky",""),
                DayWeather("Fri day","egypt",45,33,"clear sky",""),
                DayWeather("satr day","egypt",45,33,"clear sky","")
            ),
            mutableListOf<HourWeather>(
                HourWeather("egypt","12PM",33,""),
                HourWeather("egypt","1AM",33,""),
                HourWeather("egypt","2AM",33,""),
                HourWeather("egypt","3AM",33,""),
                HourWeather("egypt","4AM",33,""),
                HourWeather("egypt","5AM",33,""),
                HourWeather("egypt","6AM",33,""),
                HourWeather("egypt","7AM",33,""),
                HourWeather("egypt","8AM",33,""),
                HourWeather("egypt","9AM",33,""),
                HourWeather("egypt","10AM",33,""),
                HourWeather("egypt","11AM",33,""),
                HourWeather("egypt","12AM",33,""),
                HourWeather("egypt","1PM",33,""),
                HourWeather("egypt","2PM",33,""),
                HourWeather("egypt","3PM",33,""),
                HourWeather("egypt","4PM",33,""),
                HourWeather("egypt","5PM",33,""),
                HourWeather("egypt","6PM",33,""),
                HourWeather("egypt","7PM",33,""),
                HourWeather("egypt","8PM",33,""),
                HourWeather("egypt","9PM",33,""),
                HourWeather("egypt","10PM",33,""),
                HourWeather("egypt","11PM",33,"")
            ))
          localSourceImpl.insertLocation(locationData)
        //When
        localSourceImpl.deleteLocation(locationData.location)
        //Then
        val res=localSourceImpl.getCurrentLocation(true).asLiveData().getOrAwaitValue {  }
        assertThat(res,`is`(nullValue()))
    }

    @Test
    fun deleteAlert_checkDeleted()=mainRule.runBlockingTest {

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
        localSourceImpl.insertAlert(alert)

        //When
        localSourceImpl.deleteAlert(alert.getId()!!)
        //Then
        val res=localSourceImpl.getAlert(alert.address,alert.type)
        assertThat(res,`is`(nullValue()))
    }

    @Test
    fun getListOfFavLocations_returnListFavorites()=mainRule.runBlockingTest {
        //Given
        val locationData=LocationData(location = Location(
            "en","5/4/2023",
            "egypt","clear sky",
            true,15,10,
            2,12,"",true,1),
            mutableListOf<DayWeather>(
                DayWeather("sun day","egypt",45,33,"clear sky",""),
                DayWeather("mon day","egypt",45,33,"clear sky",""),
                DayWeather("teuth day","egypt",45,33,"clear sky",""),
                DayWeather("Wedns day","egypt",45,33,"clear sky",""),
                DayWeather("Thurs day","egypt",45,33,"clear sky",""),
                DayWeather("Fri day","egypt",45,33,"clear sky",""),
                DayWeather("satr day","egypt",45,33,"clear sky","")
            ),
            mutableListOf<HourWeather>(
                HourWeather("egypt","12PM",33,""),
                HourWeather("egypt","1AM",33,""),
                HourWeather("egypt","2AM",33,""),
                HourWeather("egypt","3AM",33,""),
                HourWeather("egypt","4AM",33,""),
                HourWeather("egypt","5AM",33,""),
                HourWeather("egypt","6AM",33,""),
                HourWeather("egypt","7AM",33,""),
                HourWeather("egypt","8AM",33,""),
                HourWeather("egypt","9AM",33,""),
                HourWeather("egypt","10AM",33,""),
                HourWeather("egypt","11AM",33,""),
                HourWeather("egypt","12AM",33,""),
                HourWeather("egypt","1PM",33,""),
                HourWeather("egypt","2PM",33,""),
                HourWeather("egypt","3PM",33,""),
                HourWeather("egypt","4PM",33,""),
                HourWeather("egypt","5PM",33,""),
                HourWeather("egypt","6PM",33,""),
                HourWeather("egypt","7PM",33,""),
                HourWeather("egypt","8PM",33,""),
                HourWeather("egypt","9PM",33,""),
                HourWeather("egypt","10PM",33,""),
                HourWeather("egypt","11PM",33,"")
            ))
        //When
        localSourceImpl.insertLocation(locationData)
        val result=localSourceImpl.getListOfFavLocations(true).asLiveData().getOrAwaitValue{}

        //Then
        assertThat(result,`is`(not(nullValue())))
    }

    @Test
    fun updateAlert_updateNumerOfDaysInAlert() =mainRule.runBlockingTest{
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
        localSourceImpl.insertAlert(alert)
        //When
        alert.numOfDays-=1
        localSourceImpl.updateAlert(alert)
        //Then
        val res=localSourceImpl.getListOfAlerts().asLiveData().getOrAwaitValue{}
        assertThat(res,`is`(notNullValue()))
        assertThat(res.size,`is`(1))
        assertThat(res[0].numOfDays,`is`(0))
    }
}