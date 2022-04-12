package com.example.weather_clean_mvi_rx.injection

import com.example.weather_clean_mvi_rx.data.network.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun weatherService(retrofit: Retrofit): WeatherService = retrofit.create(WeatherService::class.java)
}