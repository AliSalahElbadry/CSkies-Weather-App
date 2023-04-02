package com.app.our.cskies

import com.app.our.cskies.Repository.FakeLocalSource
import com.app.our.cskies.Repository.FakeRemoteSource
import com.app.our.cskies.Repository.RepositoryInterface
import com.app.our.cskies.dp.model.Alert
import com.app.our.cskies.dp.model.DayWeather
import com.app.our.cskies.dp.model.HourWeather
import com.app.our.cskies.dp.model.Location
import com.app.our.cskies.model.LocationData
import com.app.our.cskies.network.ApiState
import kotlinx.coroutines.flow.Flow

class FakeRepository(private val local:FakeLocalSource,private val remote:FakeRemoteSource):RepositoryInterface {

    override suspend fun getWeatherData(
        latitude: String,
        longitude: String,
        execlude: String,
        units: String,
        lang: String
    ): Flow<ApiState> {
        return  remote.getWeatherData(latitude,longitude,execlude,units,lang)
    }

    override suspend fun insertLocation(location: LocationData) {
       local.insertLocation(location)
    }

    override suspend fun insertAlert(alert: Alert) {
        local.insertAlert(alert)
    }

    override suspend fun deleteLocation(location: Location) {
        local.deleteLocation(location)
    }

    override suspend fun deleteAlert(id: Int) {
        local.deleteAlert(id)
    }

    override fun getListOfFavLocations(fav: Boolean): Flow<List<Location>> {
        return local.getListOfFavLocations(fav)
    }

    override fun selectDaysOfLocation(address: String): Flow<List<DayWeather>> {
       return local.selectDaysOfLocation(address)
    }

    override fun selectHoursInLocation(address: String): Flow<List<HourWeather>> {
        return local.selectHoursInLocation(address)
    }

    override fun getCurrentLocation(isCurrent: Boolean): Flow<Location> {
        return local.getCurrentLocation(isCurrent)
    }

    override fun getListOfAlerts(): Flow<List<Alert>> {
        return local.getListOfAlerts()
    }

    override suspend fun getAlert(address: String, type: Int): Alert {
      return local.getAlert(address,type)
    }

    override suspend fun updateAlert(alert: Alert) {
        local.updateAlert(alert)
    }
}