package com.hatfat.cards.info

import com.hatfat.cards.data.card.SingleCardData

interface InfoScreenDataProvider {
    fun getInfoScreenDataFromCard(cardData: SingleCardData): InfoScreenData
}
