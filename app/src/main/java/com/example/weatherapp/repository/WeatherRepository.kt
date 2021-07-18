package com.example.weatherapp.repository

import android.content.Context
import androidx.work.*
import com.example.chatapplication.di.helper.WorkMangerHelper
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.network.NetworkHelper
import com.example.weatherapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class WeatherRepository constructor(
    private val weatherApplication: Context,
    private val networkHelper: NetworkHelper,
    private val workManager: WorkManager,
    private val periodicWorkRequest: PeriodicWorkRequest
) {

    suspend fun getWeather(lat: Double, lon: Double) = withContext(Dispatchers.IO) {
        if (!WorkMangerHelper.getWorkStatus(weatherApplication))
            workManager.enqueueUniquePeriodicWork(
                Constants.IS_PERIODIC_WORK,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
            )
        networkHelper.weatherApi.getWeather(lat, lon, Constants.API_KEY)
    }
}