package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.network.WeatherDetails
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.*

class WeatherLookupViewModel(val weatherRepository: WeatherRepository, val dispatcher:CoroutineDispatcher) : ViewModel() {

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    val viewModelScope = CoroutineScope(dispatcher + SupervisorJob())

    var showProgress: MutableLiveData<Boolean>
    var showStatus: MutableLiveData<String>
    val weatherDetails: MutableLiveData<WeatherDetails>

    companion object {
        private var instance : WeatherLookupViewModel? = null
        fun getInstance(weatherRepository: WeatherRepository, dispatchers: CoroutineDispatcher) =
            instance ?: synchronized(WeatherLookupViewModel::class.java){
                instance ?: WeatherLookupViewModel(weatherRepository, dispatchers).also { instance = it }
            }
    }

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        showProgress = MutableLiveData()
        showStatus = MutableLiveData("")
        weatherDetails = MutableLiveData<WeatherDetails>()
    }

    fun lookup(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                showProgress.postValue(true)
                val response = weatherRepository.getWeather(lat, lon)
                weatherDetails.postValue(response)
                showProgress.postValue(false)
            } catch (e: Exception) {
                e.printStackTrace()
                showStatus.postValue(e.message)
                showProgress.postValue(false)
            }
        }
    }
}