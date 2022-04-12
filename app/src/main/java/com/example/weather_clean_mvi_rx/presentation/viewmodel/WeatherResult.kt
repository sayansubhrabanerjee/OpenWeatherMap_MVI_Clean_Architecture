package com.example.weather_clean_mvi_rx.presentation.viewmodel

import com.example.weather_clean_mvi_rx.domain.model.Weather

sealed class WeatherResult {
    object Terminate : WeatherResult()
    sealed class UiUpdate : WeatherResult() {
        object Loading : UiUpdate()
        data class Success(val weathers: List<Weather>) : UiUpdate()
        data class Error(val throwable: Throwable?) : UiUpdate()
    }
}
