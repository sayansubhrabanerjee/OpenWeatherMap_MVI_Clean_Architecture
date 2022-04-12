package com.example.weather_clean_mvi_rx.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weather_clean_mvi_rx.domain.model.Weather
import com.example.weather_clean_mvi_rx.domain.usecase.GetWeatherForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WeatherScreen @Inject constructor(
    private val weatherForecastUseCase: GetWeatherForecastUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val inputEventDisposables = CompositeDisposable()

    private val inputEvents: Subject<WeatherInputEvent> = PublishSubject.create()
    private val terminalEvents: Subject<WeatherResult.Terminate> = PublishSubject.create()

    private var _uiUpdate = MutableStateFlow<WeatherViewState.UiUpdate>(WeatherViewState.UiUpdate.Empty)
    val uiUpdate: StateFlow<WeatherViewState.UiUpdate> = _uiUpdate.asStateFlow()

    private var _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather.asStateFlow()

    init {
        inputEvents
            .compose(inputEventsToActions())
            .publish {
                Observable.mergeArray(
                    it.ofType(WeatherAction.GetForecastWeather::class.java).compose(
                        getWeatherForecastResults(weatherForecastUseCase)
                    )
                )
            }.compose(resultsToNewViewState(terminalEvents))
            .subscribe {
                when (it) {
                    is WeatherViewState.UiUpdate -> _uiUpdate.value = it
                }
            }.run(disposables::add)
    }

    fun setInputEvents(inputs: Observable<WeatherInputEvent>?) {
        inputEventDisposables.clear()
        inputs?.let {
            inputEventDisposables.add(
                it.subscribe(inputEvents::onNext)
            )
        }
    }

    fun setWeather(weather: Weather) {
        _weather.value = weather
    }

    override fun onCleared() {
        inputEventDisposables.clear()
        disposables.clear()
        terminalEvents.onNext(WeatherResult.Terminate)
        super.onCleared()
    }

    companion object {
        private fun inputEventsToActions(): ObservableTransformer<WeatherInputEvent, WeatherAction> =
            ObservableTransformer { events ->
                events.publish {
                    Observable.mergeArray(
                        it.compose(onScreenLoaded()),
                    )
                }
            }

        private fun onScreenLoaded(): ObservableTransformer<WeatherInputEvent, WeatherAction> =
            ObservableTransformer { events ->
                events.ofType(WeatherInputEvent.ScreenLoaded::class.java)
                    .map { WeatherAction.GetForecastWeather }
            }

        private fun getWeatherForecastResults(
            weatherForecastUseCase: GetWeatherForecastUseCase
        ): ObservableTransformer<WeatherAction, WeatherResult> =
            ObservableTransformer { actions ->
                actions.flatMap {
                    weatherForecastUseCase.getWeatherForecast()
                        .map<WeatherResult> { WeatherResult.UiUpdate.Success(it) }
                        .onErrorResumeNext { Single.just(WeatherResult.UiUpdate.Error(it)) }
                        .toObservable()
                        .startWithItem(WeatherResult.UiUpdate.Loading)
                }
            }

        private fun resultsToNewViewState(terminalEvents: Subject<WeatherResult.Terminate>): ObservableTransformer<WeatherResult, WeatherViewState> =
            ObservableTransformer<WeatherResult, WeatherViewState> { results ->
                val sharedResults = results
                    .mergeWith(terminalEvents)
                    .takeUntil { it is WeatherResult.Terminate }
                    .publish()
                    .autoConnect()
                val models = sharedResults
                    .ofType(WeatherResult.UiUpdate::class.java)
                    .compose(resultsToNewUiState())

                Observable.mergeArray(models)
            }

        private fun resultsToNewUiState(): ObservableTransformer<WeatherResult.UiUpdate, WeatherViewState.UiUpdate> =
            ObservableTransformer {
                it.map { uiState ->
                    when (uiState) {
                        WeatherResult.UiUpdate.Loading -> WeatherViewState.UiUpdate.Loading
                        is WeatherResult.UiUpdate.Success -> WeatherViewState.UiUpdate.Success(uiState.weathers)
                        is WeatherResult.UiUpdate.Error -> WeatherViewState.UiUpdate.Error(uiState.throwable)
                    }
                }
            }
    }
}