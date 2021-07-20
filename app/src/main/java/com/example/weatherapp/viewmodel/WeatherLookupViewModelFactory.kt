package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.di.component.AppComponent
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class WeatherLookupViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var weatherRepository: WeatherRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val appComponent: AppComponent = WeatherApplication.appComponent
        if (modelClass.isAssignableFrom(WeatherLookupViewModel::class.java)) {
            appComponent.inject(this)
            @Suppress("UNCHECKED_CAST")
            return WeatherLookupViewModel.getInstance(weatherRepository, Dispatchers.IO) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }

    companion object {
        private var instance : WeatherLookupViewModelFactory? = null
        fun getInstance() =
            instance ?: synchronized(WeatherLookupViewModelFactory::class.java){
                instance ?: WeatherLookupViewModelFactory().also { instance = it }
            }
    }
}