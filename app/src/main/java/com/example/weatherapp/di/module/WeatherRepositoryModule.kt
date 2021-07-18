package com.example.weatherapp.di.module

import android.content.Context
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.weatherapp.network.NetworkHelper
import com.example.weatherapp.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class WeatherRepositoryModule {

    @Provides
    @Singleton
    fun providesWeatherRepository(@Named("appcontext") context: Context, networkHelper: NetworkHelper, workManager: WorkManager,
                                  periodicWorkRequest: PeriodicWorkRequest): WeatherRepository {
        return WeatherRepository(context, networkHelper, workManager, periodicWorkRequest)
    }
}
