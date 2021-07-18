package com.example.weatherapp.network

import android.content.Context
import com.example.weatherapp.inerfaces.WeatherAPI
import com.example.weatherapp.util.Constants.Companion.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class NetworkHelper @Inject constructor(@Named("appcontext") context: Context) {

    private lateinit var retrofit: Retrofit

    object BigDecimalAdapter {
        @FromJson
        fun fromJson(string: String) = BigDecimal(string)

        @ToJson
        fun toJson(value: BigDecimal) = value.toString()
    }

    var cacheSize = 10 * 1024 * 1024L // 10 MB
    var cache: Cache = Cache(context.getCacheDir(), cacheSize)

    var cacheControl = CacheControl.Builder()
        .maxStale(2, TimeUnit.HOURS)
        .build()

    private val httpClient = OkHttpClient.Builder() //here we can add Interceptor for dynamical adding headers
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .cache(cache)
            .addNetworkInterceptor(onlineInterceptor())
            .addInterceptor(offlineInterceptor())
            .connectTimeout(3, TimeUnit.SECONDS)
            .build()

    private fun onlineInterceptor() = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .build()
        chain.proceed(request)
    }

    private fun offlineInterceptor() = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .cacheControl(cacheControl)
            .build()
        chain.proceed(request)
    }

    val weatherApi by lazy {
        retrofit.create(WeatherAPI::class.java)
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(Moshi.Builder()
                .add(BigDecimalAdapter)
                .add(KotlinJsonAdapterFactory())
                .build()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient)
            .build()
    }
}
