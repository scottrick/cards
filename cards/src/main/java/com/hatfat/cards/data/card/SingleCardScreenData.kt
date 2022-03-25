package com.hatfat.cards.data.card

import android.widget.ImageView

data class SingleCardScreenData(
    val cardImageLoader: (imageView: ImageView) -> Unit,
)