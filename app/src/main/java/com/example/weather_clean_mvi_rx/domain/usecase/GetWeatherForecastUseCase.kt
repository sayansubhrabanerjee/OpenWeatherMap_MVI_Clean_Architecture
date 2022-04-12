package com.example.weather_clean_mvi_rx.domain.usecase

import com.example.weather_clean_mvi_rx.common.util.RxSchedulers
import com.example.weather_clean_mvi_rx.domain.model.Weather
import com.example.weather_clean_mvi_rx.domain.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetWeatherForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val rxSchedulers: RxSchedulers
) {
    fun getWeatherForecast(): Single<List<Weather>> =
        weatherRepository.getWeatherForecast().map {
            it.data!!.sortedBy { weather -> weather.maxTemp }
        }.subscribeOn(rxSchedulers.computationScheduler)
}