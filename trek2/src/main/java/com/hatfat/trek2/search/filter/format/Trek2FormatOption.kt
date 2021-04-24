package com.hatfat.trek2.search.filter.format

import com.hatfat.cards.search.filter.SpinnerOption

data class Trek2FormatOption(
    val name: String,
    val optionType: Trek2FormatOptionType,
) : SpinnerOption {
    override val displayName: String
        get() = name
}