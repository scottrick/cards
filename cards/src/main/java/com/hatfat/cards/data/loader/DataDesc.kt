package com.hatfat.cards.data.loader

import androidx.annotation.RawRes

data class DataDesc<T>(
    val type: Class<T>,
    val networkLoader: suspend () -> T,
    @RawRes val resourceId: Int,
    val fallbackValue: T,
    val name: String,
)
