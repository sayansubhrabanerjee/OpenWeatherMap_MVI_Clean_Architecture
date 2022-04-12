package com.example.weather_clean_mvi_rx.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val ids: List<Int>,
    val maxTemp: Double,
    val mainTitles: List<String>,
    val descriptions: List<String>,
    val icons: List<String>,
) : Parcelable
