package com.example.weather_clean_mvi_rx.injection

import com.example.weather_clean_mvi_rx.data.datasource.remote.WeatherRemoteDataSource
import com.example.weather_clean_mvi_rx.data.datasource.remote.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    fun weatherRemoteDataSource(impl: WeatherRemoteDataSourceImpl): WeatherRemoteDataSource
}