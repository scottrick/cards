package com.hatfat.swccg.app

import com.hatfat.cards.app.CardsConfig
import javax.inject.Inject

class SWCCGCardsConfig @Inject constructor() : CardsConfig {
    override val shouldDisplayInfoButton: Boolean
        get() = true

    override val shouldDisplayFlipButton: Boolean
        get() = true
}