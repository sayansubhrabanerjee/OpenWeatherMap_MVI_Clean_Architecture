package com.example.weather_clean_mvi_rx.injection

import com.example.weather_clean_mvi_rx.data.repository.WeatherRepositoryImpl
import com.example.weather_clean_mvi_rx.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun weatherRepository(impl: WeatherRepositoryImpl): WeatherRepository
}