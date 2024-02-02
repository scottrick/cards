package com.hatfat.cards.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation

data class CardTransformations(
    val transformations: List<Transformation<Bitmap>>
)
