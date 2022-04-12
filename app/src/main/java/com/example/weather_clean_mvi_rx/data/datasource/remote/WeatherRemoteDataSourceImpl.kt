package com.example.weather_clean_mvi_rx.data.datasource.remote

import com.example.weather_clean_mvi_rx.data.network.model.WeatherResponse
import com.example.weather_clean_mvi_rx.data.network.service.WeatherService
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService
): WeatherRemoteDataSource {
    override fun getWeatherForecast(): Single<Response<WeatherResponse>> =
        weatherService.getWeatherForecast(dayCount = 16)
}