package com.example.chatapplication.di.component

import com.example.chatapplication.di.module.AppContextModule
import com.example.weatherapp.network.NetworkHelper
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.di.module.WeatherRepositoryModule
import com.example.weatherapp.di.module.WorkManagerModule
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.viewmodel.WeatherLookupViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppContextModule::class, WeatherRepositoryModule::class, WorkManagerModule::class])
interface AppComponent {

    fun getNetworkHelper(): NetworkHelper

    fun getWeatherRepository(): WeatherRepository

    fun inject(application: WeatherApplication)

    fun inject(weatherLookupViewModelFactory: WeatherLookupViewModelFactory)
}
