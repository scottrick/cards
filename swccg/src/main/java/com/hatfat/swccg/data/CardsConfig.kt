package com.hatfat.swccg.data

import java.io.Serializable

data class CardsConfig(
    /* PlayStore images are greatly reduced in resolution */
    val shouldUsePlayStoreImages: Boolean = false
) : Serializable
