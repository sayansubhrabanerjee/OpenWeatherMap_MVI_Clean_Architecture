package com.example.weather_clean_mvi_rx.common.util

import android.content.Context
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.weather_clean_mvi_rx.presentation.ui.CustomImageView

object GlideConfig {
    fun config(context: Context, url: String, imageView: CustomImageView) {
        GlideApp
            .with(context)
            .applyDefaultRequestOptions(requestOptions)
            .load(url)
            .into(imageView)
    }

    private val requestOptions: RequestOptions =
        RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .downsample(DownsampleStrategy.AT_LEAST)
            .centerCrop()
}