package com.hatfat.trek1.search.filter.set

import com.hatfat.cards.search.filter.SpinnerOption

data class Trek1SetOption(
    val name: String,
    val code: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}