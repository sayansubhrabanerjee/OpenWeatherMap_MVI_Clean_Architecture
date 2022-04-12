package com.example.weather_clean_mvi_rx.presentation.viewmodel

sealed class WeatherInputEvent {
    object ScreenLoaded : WeatherInputEvent()
}
