package com.mertechtest.fishingplanner.data.network

import com.mertechtest.fishingplanner.BuildConfig
import com.mertechtest.fishingplanner.data.model.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface NetworkAPI {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") q: String,
        @Query("dt") dt: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = BuildConfig.weather_api_key
    ): Response<WeatherResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: Interceptor
        ): NetworkAPI {

            val weatherServerUrl = BuildConfig.end_point
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(weatherServerUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NetworkAPI::class.java)
        }
    }

}