package com.hatfat.cards.info

import android.widget.ImageView

data class InfoScreenData(
    val title: String,
    val infoTitle: String,
    val infoList: List<String>,
    val cardFrontImageLoader: (imageView: ImageView) -> Unit,
    val cardBackImageLoader: (imageView: ImageView) -> Unit,
)