package com.example.weather_clean_mvi_rx.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_clean_mvi_rx.databinding.FragmentWeathersBinding
import com.example.weather_clean_mvi_rx.domain.model.Weather
import com.example.weather_clean_mvi_rx.presentation.adapter.WeatherAdapter
import com.example.weather_clean_mvi_rx.presentation.viewmodel.WeatherInputEvent
import com.example.weather_clean_mvi_rx.presentation.viewmodel.WeatherScreen
import com.example.weather_clean_mvi_rx.presentation.viewmodel.WeatherViewState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WeathersFragment : Fragment() {

    private var _binding: FragmentWeathersBinding? = null
    private val binding get() = _binding!!

    private val screen: WeatherScreen by activityViewModels()

    private val inputEvents: Subject<WeatherInputEvent> = PublishSubject.create()
    private val disposables = CompositeDisposable()

    private var weatherAdapter: WeatherAdapter? = null
    private var weathers: List<Weather> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeathersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screen.setInputEvents(inputEvents)

        setAdapter()

        inputEvents.onNext(WeatherInputEvent.ScreenLoaded)

        lifecycleScope.launchWhenStarted {
            screen.uiUpdate.collectLatest {
                render(it)
            }
        }

        subscribeViewEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherAdapter = null
        disposables.clear()
        _binding = null
    }

    private fun subscribeViewEvents() {
        disposables.add(
            Observable.mergeArray(
                inputEvents,
                //binding.errorView.clicks().map { WeatherInputEvent.ScreenLoaded }
            ).subscribe { inputEvents.onNext(it) }
        )
    }

    private fun render(weatherViewState: WeatherViewState) {
        when (weatherViewState) {

            WeatherViewState.UiUpdate.Empty -> {
                binding.errorView.isVisible = false
                binding.progressBar.isVisible = false
                binding.recyclerViewWeathers.isVisible = false
            }

            WeatherViewState.UiUpdate.Loading -> {
                binding.errorView.isVisible = false
                binding.progressBar.isVisible = true
                binding.recyclerViewWeathers.isVisible = false
            }

            is WeatherViewState.UiUpdate.Success -> {
                binding.errorView.isVisible = false
                binding.progressBar.isVisible = false
                binding.recyclerViewWeathers.isVisible = true
                weatherAdapter?.updateWeathers(weatherViewState.weathers)
            }

            is WeatherViewState.UiUpdate.Error -> {
                binding.errorView.isVisible = true
                binding.progressBar.isVisible = false
                binding.recyclerViewWeathers.isVisible = false
                Log.e("mytest: ", "Error received: ${weatherViewState.throwable?.localizedMessage}")
            }
        }
    }

    private fun setAdapter() {
        weatherAdapter = WeatherAdapter()
        binding.recyclerViewWeathers.apply {
            adapter = weatherAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
        }
        weatherAdapter?.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            updateWeathers(weathers)
            disposables.add(clickEvents().subscribe {
                when (it) {
                    is WeatherAdapter.ClickEvents.OnWeatherItemClicked -> {
                        screen.setWeather(it.weather)
                        val action = WeathersFragmentDirections.actionWeathersFragmentToWeatherDetailsFragment()
                        findNavController().navigate(action)
                    }
                }
            })
        }
    }
}