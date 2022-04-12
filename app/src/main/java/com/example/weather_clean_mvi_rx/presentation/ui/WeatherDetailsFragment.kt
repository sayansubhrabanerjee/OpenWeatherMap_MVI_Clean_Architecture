package com.example.weather_clean_mvi_rx.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.weather_clean_mvi_rx.common.util.GlideConfig
import com.example.weather_clean_mvi_rx.databinding.FragmentWeatherDetailsBinding
import com.example.weather_clean_mvi_rx.presentation.viewmodel.WeatherScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WeatherDetailsFragment : Fragment() {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!

    private val screen: WeatherScreen by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            updateUi()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun updateUi() {
        screen.weather.collectLatest {
            it?.icons?.forEach { icon ->
                GlideConfig.config(
                    context = requireContext(),
                    url = "https://openweathermap.org/img/wn/${icon}@2x.png",
                    imageView = binding.imageViewIcon
                )
            }
        }
    }
}