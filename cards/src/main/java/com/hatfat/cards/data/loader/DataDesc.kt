package com.hatfat.cards.data.loader

import androidx.annotation.RawRes
import com.google.gson.reflect.TypeToken

data class DataDesc<T>(
    val typeToken: TypeToken<T>,
    val networkLoader: suspend () -> T,
    @RawRes val resourceId: Int,
    val fallbackValue: T,
    val name: String,
)
