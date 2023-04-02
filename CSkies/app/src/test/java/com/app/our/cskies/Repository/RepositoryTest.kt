package com.app.our.cskies.Repository

import com.app.our.cskies.MainRule
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.ApiState
import com.app.our.cskies.network.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainRule = MainRule()

    lateinit var fakeLocalSource: FakeLocalSource
    lateinit var fakeRemoteSource: FakeRemoteSource
    lateinit var repository: Repository
    lateinit var location:Location
    lateinit var days:MutableList<DayWeather>
    lateinit var hours:MutableList<HourWeather>
    lateinit var weatherLocationData: WeatherLocationData

    @Before
    fun setUp() {
         location = Location(
            "en","5/4/2023",
            "egypt","clear sky",
            true,15,10,
            2,12,"",true,1)
         days= mutableListOf<DayWeather>(
            DayWeather("sun day","egypt",45,33,"clear sky",""),
            DayWeather("mon day","egypt",45,33,"clear sky",""),
            DayWeather("teuth day","egypt",45,33,"clear sky",""),
            DayWeather("Wedns day","egypt",45,33,"clear sky",""),
            DayWeather("Thurs day","egypt",45,33,"clear sky",""),
            DayWeather("Fri day","egypt",45,33,"clear sky",""),
            DayWeather("satr day","egypt",45,33,"clear sky","")
        )
         hours= mutableListOf<HourWeather>(
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
        )
        fakeLocalSource= FakeLocalSource(mutableListOf(), mutableListOf(), mutableListOf(),
            mutableListOf()
        )

        weatherLocationData= WeatherLocationData(
            Current(1,1.0,122546,1.2,1,1, Rain(1.1),1,1,1.0,1.0,1,
            listOf(Weather("clear Sky","10d",0,"")),1,1.0,1.0),
            listOf(),
            listOf(),31.00123,30.1223, listOf(),"",0, listOf()
                )
        fakeRemoteSource= FakeRemoteSource(weatherLocationData)
        repository=Repository.getInstance(fakeLocalSource,fakeRemoteSource)
    }
    @Test
    fun GetWeatherData_returnWeatherLocationData() = mainRule.runBlockingTest {
            repository.getWeatherData(
                weatherLocationData.lat.toString(),
                weatherLocationData.lon.toString()
            ).collect{
                   if(it is  ApiState.Success)
                    assertThat(it.locationData, CoreMatchers.`is`(weatherLocationData))

            }
        }


    @Test
    fun Location_InsertCheckInserted() = mainRule.runBlockingTest{
        launch {
            repository.insertLocation(
                LocationData(
                    location.apply { isCurrent = true },
                    days,
                    hours
                )
            )
        }.join()
        launch {
            repository.getCurrentLocation(true).collect {
                assertThat(it ,CoreMatchers.`is`(not(nullValue())))
            }
        }

    }

    @Test
    fun Alert_InsertCheckInserted() = mainRule.runBlockingTest{
        val alert=com.app.our.cskies.dp.model.Alert("","",false,false,false,false,false,"11","123","123",10)
        launch {
            repository.insertAlert(
               alert
            )
        }.join()
        launch {
                assertThat(repository.getAlert(alert.address,alert.type) ,CoreMatchers.`is`(not(nullValue())))
            }
    }
    @Test
    fun Location_GetcheckTrue_returnListOfFavorites() = mainRule.runBlockingTest{
        launch {
            repeat(10)
            {
                repository.insertLocation(LocationData(location.apply { isFavorite=true },days,hours))
            }
        }.join()
        launch {
            var res=false
            repository.getListOfFavLocations(true).collect{ it ->
                it.forEach{l->
                    res=l.isFavorite
                }
            }
            assertThat(res,CoreMatchers.`is`(true))
        }
    }

    @Test
    fun Location_GetCheckTrue_returnListOfDays_returnLocationDays() = mainRule.runBlockingTest{
        launch {
            repository.insertLocation(LocationData(location,days,hours))
        }.join()
        launch {
            repository.selectDaysOfLocation(location.address).collect{
                assertThat(it,CoreMatchers.`is`(not(nullValue())))
            }
        }
    }

    @Test
    fun Location_GetCheckTrue_returnListOfHours_returnLocationHours()=mainRule.runBlockingTest{
        launch {
            repository.insertLocation(LocationData(location,days,hours))
        }.join()
        launch {
            repository.selectHoursInLocation(location.address).collect{
                assertThat(it,CoreMatchers.`is`(not(nullValue())))
            }
        }
    }

    @Test
    fun LocationgetCurrentCeckTrue_returnCurrentLocation() =mainRule.runBlockingTest{
        launch {
            repository.insertLocation(LocationData(location.apply { isCurrent=true },days,hours))
        }.join()
        launch {
            repository.getCurrentLocation(true).collect{
                assertThat(it.isCurrent,CoreMatchers.`is`(true))
            }
        }
    }

    @Test
    fun ListOfAlertsgetCheckTrue_returnListAlerts() =mainRule.runBlockingTest{

        launch {
            repeat(10) {
                repository.insertAlert(
                    com.app.our.cskies.dp.model.Alert(
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
                )
            }
        }
        launch {
            repository.getListOfAlerts().collect{
                assertThat(it,CoreMatchers.`is`(not(nullValue())))
            }
        }
        advanceUntilIdle()
    }


    @Test
    fun Alert_UpdateCeckTrue()= runTest{
        val alert= com.app.our.cskies.dp.model.Alert(
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
        launch {
            repository.insertAlert(alert)
        }.job.join()
        launch {
            alert.numOfDays-=1
            repository.updateAlert(alert)
        }
        advanceUntilIdle()
          val res=repository.getAlert(alert.address,alert.type)
          assertThat(res.numOfDays,CoreMatchers.`is`(0))


    }
}