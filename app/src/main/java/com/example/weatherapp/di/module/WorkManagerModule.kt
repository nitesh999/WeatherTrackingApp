package com.example.weatherapp.di.module

import android.content.Context
import androidx.work.*
import com.example.weatherapp.util.Constants
import com.example.weatherapp.works.FetchWeatherWork
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class WorkManagerModule {

    @Provides
    @Singleton
    fun providesWorkManager(@Named("appcontext") context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun providesPeriodicWork(): PeriodicWorkRequest {
        return PeriodicWorkRequest.Builder(
            FetchWeatherWork::class.java, 2, TimeUnit.HOURS)
            .setConstraints(constraints)
            .addTag(Constants.IS_PERIODIC_WORK)
            .build()
    }

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()
}