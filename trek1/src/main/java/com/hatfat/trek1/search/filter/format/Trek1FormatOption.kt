package com.hatfat.trek1.search.filter.format

import com.hatfat.cards.search.filter.SpinnerOption

data class Trek1FormatOption(
    val name: String,
    val optionType: Trek1FormatOptionType,
) : SpinnerOption {
    override val displayName: String
        get() = name
}