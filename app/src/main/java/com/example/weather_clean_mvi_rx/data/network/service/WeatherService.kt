package com.example.weather_clean_mvi_rx.data.network.service

import com.example.weather_clean_mvi_rx.BuildConfig
import com.example.weather_clean_mvi_rx.data.network.model.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("forecast/daily")
    fun getWeatherForecast(
        @Query("lat") lat: Double = 12.97,
        @Query("lon") lon: Double = 77.59,
        @Query("cnt") dayCount: Int,
        @Query("appid") appId: String = BuildConfig.API_KEY,
    ): Single<Response<WeatherResponse>>
}