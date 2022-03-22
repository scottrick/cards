package com.hatfat.meccg.app

import com.hatfat.cards.app.CardsConfig
import javax.inject.Inject

class MECCGCardsConfig @Inject constructor() : CardsConfig {
    override val shouldDisplayInfoButton: Boolean
        get() = false

    override val shouldDisplayFlipButton: Boolean
        get() = false
}
