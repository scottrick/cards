package com.hatfat.trek1.search.filter.type

import com.hatfat.cards.search.filter.SpinnerOption

data class Trek1TypeOption(
    val name: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}