package com.app.our.cskies.favorites.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.our.cskies.FakeRepository
import com.app.our.cskies.MainRule
import com.app.our.cskies.Repository.FakeLocalSource
import com.app.our.cskies.Repository.FakeRemoteSource
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.getOrAwaitValue
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.model.Current
import com.app.our.cskies.network.model.Rain
import com.app.our.cskies.network.model.Weather
import com.app.our.cskies.network.model.WeatherLocationData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class FavoritesViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainRule = MainRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var favoritesViewModel: FavoritesViewModel
    lateinit var fakeRepository: FakeRepository
    lateinit var fakeLocalSource: FakeLocalSource
    lateinit var fakeRemoteSource: FakeRemoteSource
    lateinit var location: Location
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
        fakeRepository= FakeRepository(fakeLocalSource,fakeRemoteSource)

        favoritesViewModel= FavoritesViewModel(fakeRepository)
    }

    @Test
    fun getAllFavoriteLocations()= mainRule.runBlockingTest{
        //Before
        repeat(10) {
            favoritesViewModel.insertLocation(
                LocationData(
                    location.apply { isFavorite = true },
                    days,
                    hours
                )
            )
        }
        //When
        favoritesViewModel.getAllFavoriteLocations()

        //Then
        val value= favoritesViewModel.liveData.getOrAwaitValue { }
        assertThat(value,CoreMatchers.`is`(not(nullValue())))
    }

    @Test
    fun getLocationData()=mainRule.runBlockingTest {
        //Before
        favoritesViewModel.insertLocation(LocationData(location.apply { isFavorite=true },days,hours))

        //When
        favoritesViewModel.getLocationData(location.apply { isFavorite=true })

        //Then
        val value=favoritesViewModel.locationData.getOrAwaitValue {  }
        assertThat(value,CoreMatchers.`is`(not(nullValue())))
    }

    @Test
    fun deleteLocation()=mainRule.runBlockingTest{
        //Before
        favoritesViewModel.insertLocation(LocationData(location.apply { isFavorite=true },days,hours))

        //When
        favoritesViewModel.deleteLocation(location.apply { isFavorite=true })

        //Then
        favoritesViewModel.getAllFavoriteLocations()
        val value=favoritesViewModel.liveData.getOrAwaitValue{}
        assertThat(value.size,CoreMatchers.`is`(0))
    }
}