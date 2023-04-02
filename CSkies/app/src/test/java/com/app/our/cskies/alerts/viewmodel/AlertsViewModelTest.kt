package com.app.our.cskies.alerts.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.our.cskies.FakeRepository
import com.app.our.cskies.MainRule
import com.app.our.cskies.Repository.FakeLocalSource
import com.app.our.cskies.Repository.FakeRemoteSource
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.getOrAwaitValue
import com.app.our.cskies.network.model.Current
import com.app.our.cskies.network.model.Rain
import com.app.our.cskies.network.model.Weather
import com.app.our.cskies.network.model.WeatherLocationData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class AlertsViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainRule = MainRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

   private lateinit var alertsViewModel: AlertsViewModel
   private lateinit var fakeRepository: FakeRepository
   private lateinit var fakeLocalSource: FakeLocalSource
   private lateinit var fakeRemoteSource: FakeRemoteSource
   private lateinit var location: Location
   private lateinit var weatherLocationData: WeatherLocationData
   private lateinit var alert:Alert

    @Before
    fun setUp(){
       alert= Alert(
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
        alert.setId(1)
        location = Location(
            "en","5/4/2023",
            "egypt","clear sky",
            true,15,10,
            2,12,"",true,1)
        fakeLocalSource= FakeLocalSource(
            mutableListOf(), mutableListOf(), mutableListOf(),
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
        alertsViewModel= AlertsViewModel(fakeRepository)
    }
    @Test
    fun insertAlert_checkInserted()= mainRule.runBlockingTest {
        //When
        alertsViewModel.insertAlert(alert)

        //Then
        val result=alertsViewModel.alerts.getOrAwaitValue {  }
        assertThat(result.size,CoreMatchers.`is`(1))
    }

    @Test
    fun deleteAlert() = mainRule.runBlockingTest{
        //When
        alertsViewModel.insertAlert(alert)
        alertsViewModel.deleteAlert(alert)
        //Then
        val result=alertsViewModel.alerts.getOrAwaitValue {  }
        assertThat(result.size,CoreMatchers.`is`(0))
    }

    @Test
    fun getAllAlerts()= mainRule.runBlockingTest {

        //Before
        alertsViewModel.insertAlert(alert)

        //When
        alertsViewModel.getAllAlerts()

        //Then
        val result=alertsViewModel.alerts.getOrAwaitValue{}
        assertThat(result,CoreMatchers.`is`(not(nullValue())))
    }

    @Test
    fun setAlertTypes()= mainRule.runBlockingTest {

      //when
      alertsViewModel.setAlertTypes(true,true,true,true,true)

      //then
      val result=alertsViewModel.newAlert
        assertThat(result.humidity,CoreMatchers.`is`(true))
        assertThat(result.pressure,CoreMatchers.`is`(true))
        assertThat(result.temperature,CoreMatchers.`is`(true))
        assertThat(result.visibility,CoreMatchers.`is`(true))
        assertThat(result.windSpeed,CoreMatchers.`is`(true))
    }

    @Test
    fun setAlertToFrom()= runTest {
       //When
        alertsViewModel.setAlertToFrom("123321","333222",1,"egypt",1)

        //Then
        val res=alertsViewModel.newAlert
        assertThat(res.address,CoreMatchers.`is`("egypt"))
        assertThat(res.numOfDays,CoreMatchers.`is`(1))
        assertThat(res.fromDate,CoreMatchers.`is`("333222"))
        assertThat(res.toDate,CoreMatchers.`is`("123321"))
    }
}