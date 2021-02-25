package com.hatfat.cards.tabs

import java.io.Serializable

data class CardsFragmentTab(
    val tabId: Long,
    var hasTabBeenOpened: Boolean = false
) : Serializable