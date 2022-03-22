package com.hatfat.trek1.app

import com.hatfat.cards.app.CardsConfig
import javax.inject.Inject

class Trek1CardsConfig @Inject constructor() : CardsConfig {
    override val shouldDisplayInfoButton: Boolean
        get() = false

    override val shouldDisplayFlipButton: Boolean
        get() = false
}
