package com.example.weatherapp.repository

import android.content.Context
import androidx.work.*
import com.example.weatherapp.helper.WorkMangerHelper
import com.example.weatherapp.inerfaces.WeatherAPI
import com.example.weatherapp.network.NetworkHelper
import com.example.weatherapp.network.WeatherDetails
import com.example.weatherapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WeatherRepository(
    private val weatherApplication: Context,
    private val weatherApi: WeatherAPI,
    private val workManager: WorkManager,
    private val periodicWorkRequest: PeriodicWorkRequest
) {

    suspend fun getWeather(lat: Double, lon: Double): WeatherDetails? {
        try {
            if (!WorkMangerHelper.getWorkStatus(weatherApplication))
                workManager.enqueueUniquePeriodicWork(
                    Constants.IS_PERIODIC_WORK,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodicWorkRequest
                )
            return weatherApi.getWeather(lat, lon, Constants.API_KEY)
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        return null
    }
}