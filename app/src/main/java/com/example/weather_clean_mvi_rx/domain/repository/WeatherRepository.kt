package com.example.weather_clean_mvi_rx.domain.repository

import com.example.weather_clean_mvi_rx.common.util.ApiResult
import com.example.weather_clean_mvi_rx.domain.model.Weather
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {
    fun getWeatherForecast(): Single<ApiResult<List<Weather>>>
}