package com.hatfat.trek2.search.filter.set

import com.hatfat.cards.search.filter.SpinnerOption

data class Trek2SetOption(
    val name: String,
    val code: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}