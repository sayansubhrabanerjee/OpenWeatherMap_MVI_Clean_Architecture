package com.example.weather_clean_mvi_rx.data.network.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherData(
    @Json(name = "clouds")
    val clouds: Int,
    @Json(name = "deg")
    val deg: Int,
    @Json(name = "dt")
    val dt: Int,
    @Json(name = "feels_like")
    val feelsLike: FeelsLike,
    @Json(name = "gust")
    val gust: Double,
    @Json(name = "humidity")
    val humidity: Int,
    @Json(name = "pop")
    val pop: Double,
    @Json(name = "pressure")
    val pressure: Int,
    @Json(name = "speed")
    val speed: Double,
    @Json(name = "sunrise")
    val sunrise: Int,
    @Json(name = "sunset")
    val sunset: Int,
    @Json(name = "temp")
    val temp: Temp,
    @Json(name = "weather")
    val weatherEntities: List<WeatherEntity>
)