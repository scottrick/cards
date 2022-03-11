package com.hatfat.cards.info

data class InfoScreenData(
    val title: String,
    val cardFrontImageUrl: String,
    val cardBackImageUrl: String,
    val rulings: List<String>,
)