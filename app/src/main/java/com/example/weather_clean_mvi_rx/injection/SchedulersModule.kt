package com.example.weather_clean_mvi_rx.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

@Module
@InstallIn(SingletonComponent::class)
object SchedulersModule {

    @Provides
    @IOScheduler
    fun ioScheduler(): Scheduler = Schedulers.io()

    @Provides
    @MainThreadScheduler
    fun mainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @ComputationScheduler
    fun computationScheduler(): Scheduler = Schedulers.computation()
}