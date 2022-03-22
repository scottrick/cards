package com.hatfat.trek2.app

import com.hatfat.cards.app.CardsConfig
import javax.inject.Inject

class Trek2CardsConfig @Inject constructor() : CardsConfig {
    override val shouldDisplayInfoButton: Boolean
        get() = false

    override val shouldDisplayFlipButton: Boolean
        get() = false
}
