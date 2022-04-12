package com.example.weather_clean_mvi_rx.presentation.viewmodel

import com.example.weather_clean_mvi_rx.domain.model.Weather

sealed class WeatherViewState{
    /*data class UiUpdate(
        val isLoading: Boolean = false,
        val weathers: List<Weather> = emptyList(),
        val error: Throwable? = null
    ) : WeatherViewState()*/

    sealed class UiUpdate : WeatherViewState() {
        object Empty : UiUpdate()
        object Loading : UiUpdate()
        data class Success(val weathers: List<Weather>) : UiUpdate()
        data class Error(val throwable: Throwable?) : UiUpdate()
    }
}