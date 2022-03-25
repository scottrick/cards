package com.hatfat.cards.data.card

interface SingleCardScreenDataProvider {
    fun getSingleCardScreenDataFromCard(cardData: SingleCardData): SingleCardScreenData
}