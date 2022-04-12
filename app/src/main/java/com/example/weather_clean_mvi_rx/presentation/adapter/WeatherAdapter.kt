package com.example.weather_clean_mvi_rx.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_clean_mvi_rx.common.util.GlideConfig
import com.example.weather_clean_mvi_rx.databinding.WeatherItemRowBinding
import com.example.weather_clean_mvi_rx.domain.model.Weather
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class WeatherAdapter(
    private val weathers: List<Weather> = emptyList(),
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private lateinit var binding: WeatherItemRowBinding

    private val inputEvents: Subject<ClickEvents> = PublishSubject.create()

    private val differ = AsyncListDiffer<Weather>(
        this,
        DiffUtilCallback
    ).apply { submitList(weathers) }

    private object DiffUtilCallback : DiffUtil.ItemCallback<Weather>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem.ids == newItem.ids
        }

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        binding = WeatherItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = differ.currentList[position]
        holder.itemView.setOnClickListener {
            inputEvents.onNext(ClickEvents.OnWeatherItemClicked(weather))
        }
        holder.bind(binding, weather)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int = position

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(binding: WeatherItemRowBinding, weather: Weather) {
            weather.mainTitles.forEach { binding.textViewMain.text = it }
            weather.descriptions.forEach { binding.textViewDescription.text = it }
            binding.textViewTemp.text = weather.maxTemp.toString()
            weather.icons.forEach {
                GlideConfig.config(
                    context = itemView.context,
                    url = "https://openweathermap.org/img/wn/${it}@2x.png",
                    imageView = binding.imageViewIcon
                )
            }
        }
    }

    fun updateWeathers(weathers: List<Weather>) {
        differ.submitList(weathers)
    }

    fun clickEvents(): Observable<ClickEvents> = inputEvents

    sealed class ClickEvents {
        //data class OnWeatherItemClicked(val weather: Weather.Weather): ClickEvents()
        data class OnWeatherItemClicked(val weather: Weather): ClickEvents()
    }
}