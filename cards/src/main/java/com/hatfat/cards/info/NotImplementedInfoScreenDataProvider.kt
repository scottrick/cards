package com.hatfat.cards.info

import com.hatfat.cards.data.card.SingleCardData
import javax.inject.Inject

class NotImplementedInfoScreenDataProvider @Inject constructor() : InfoScreenDataProvider {
    override fun getInfoScreenDataFromCard(cardData: SingleCardData): InfoScreenData {
        throw RuntimeException("Not Implemented!")
    }
}
