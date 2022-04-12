package com.example.weather_clean_mvi_rx.injection

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainThreadScheduler
