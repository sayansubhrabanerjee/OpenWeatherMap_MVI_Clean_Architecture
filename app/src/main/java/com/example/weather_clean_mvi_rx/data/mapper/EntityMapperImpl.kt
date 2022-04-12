package com.example.weather_clean_mvi_rx.data.mapper

import com.example.weather_clean_mvi_rx.data.network.model.WeatherData
import com.example.weather_clean_mvi_rx.domain.model.Weather

object EntityMapperImpl : EntityMapper {
    override fun map(weatherData: WeatherData): Weather =
        Weather(
            ids = weatherData.weatherEntities.map { it.id },
            maxTemp = weatherData.temp.max,
            mainTitles = weatherData.weatherEntities.map { it.main },
            descriptions = weatherData.weatherEntities.map { it.description },
            icons = weatherData.weatherEntities.map { it.icon },
        )
}