package com.hatfat.trek2.search.filter.type

import com.hatfat.cards.search.filter.SpinnerOption

data class Trek2TypeOption(
    val name: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}