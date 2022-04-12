package com.example.weather_clean_mvi_rx.presentation.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.withStyledAttributes
import com.example.weather_clean_mvi_rx.R
import com.example.weather_clean_mvi_rx.databinding.WeatherCustomImageviewBinding
import javax.inject.Inject

class CustomImageView @Inject constructor(
    context: Context,
    attrs: AttributeSet
) : AppCompatImageView(context, attrs) {
    private var _binding: WeatherCustomImageviewBinding? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.CustomImageView) {
            _binding?.imageViewIcon?.setImageDrawable(
                getDrawable(R.styleable.CustomImageView_imageSrc)
            )
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}