package com.example.weather_clean_mvi_rx.data.datasource.remote

import com.example.weather_clean_mvi_rx.data.network.model.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

interface WeatherRemoteDataSource {
    fun getWeatherForecast(): Single<Response<WeatherResponse>>
}