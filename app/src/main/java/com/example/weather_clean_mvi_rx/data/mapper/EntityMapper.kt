package com.example.weather_clean_mvi_rx.data.mapper

import com.example.weather_clean_mvi_rx.data.network.model.WeatherData
import com.example.weather_clean_mvi_rx.domain.model.Weather

interface EntityMapper {
    fun map(weatherData: WeatherData): Weather
}