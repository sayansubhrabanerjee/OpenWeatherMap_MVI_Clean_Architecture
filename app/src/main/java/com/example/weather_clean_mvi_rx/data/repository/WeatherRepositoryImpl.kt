package com.example.weather_clean_mvi_rx.data.repository

import com.example.weather_clean_mvi_rx.common.util.ApiResult
import com.example.weather_clean_mvi_rx.common.util.RxSchedulers
import com.example.weather_clean_mvi_rx.data.datasource.remote.WeatherRemoteDataSource
import com.example.weather_clean_mvi_rx.data.mapper.EntityMapperImpl
import com.example.weather_clean_mvi_rx.data.network.model.WeatherResponse
import com.example.weather_clean_mvi_rx.domain.model.Weather
import com.example.weather_clean_mvi_rx.domain.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val rxSchedulers: RxSchedulers,
): WeatherRepository {
    override fun getWeatherForecast(): Single<ApiResult<List<Weather>>> =
        weatherRemoteDataSource.getWeatherForecast().map {
            convert(it)
        }.subscribeOn(rxSchedulers.ioScheduler)

    private fun convert(response: Response<WeatherResponse>): ApiResult<List<Weather>> {
        if (response.isSuccessful) {
            response.body()?.let {
                val weathers = it.weatherDataList.map { weatherData -> EntityMapperImpl.map(weatherData) }
                return ApiResult.Success(data = weathers)
            }
        }
        return ApiResult.Error(errorMessage = response.message())
    }
}