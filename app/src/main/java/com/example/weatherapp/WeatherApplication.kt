package com.example.weatherapp

import android.app.Application
import com.example.chatapplication.di.component.AppComponent
import com.example.chatapplication.di.component.DaggerAppComponent
import com.example.chatapplication.di.module.AppContextModule
import com.example.weatherapp.repository.WeatherRepository
import javax.inject.Inject

class WeatherApplication : Application() {

    companion object {
        private lateinit var _appComponent: AppComponent
        var appComponent: AppComponent
            get() = _appComponent
            set(value) {
                _appComponent = value
            }

    }

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.builder()
            .appContextModule(AppContextModule(this))
            .build()
        _appComponent.inject(this)

    }
}