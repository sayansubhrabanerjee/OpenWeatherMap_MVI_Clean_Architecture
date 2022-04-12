package com.example.weather_clean_mvi_rx.common.util

import com.example.weather_clean_mvi_rx.injection.IOScheduler
import com.example.weather_clean_mvi_rx.injection.MainThreadScheduler
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

data class RxSchedulers @Inject constructor(
    @IOScheduler val ioScheduler: Scheduler,
    @MainThreadScheduler val mainThreadScheduler: Scheduler,
    @IOScheduler val computationScheduler: Scheduler,
)
