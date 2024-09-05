package com.hatfat.meccg.search.filter.released

import com.hatfat.cards.search.filter.SpinnerOption

data class MECCGReleasedOption(
    val name: String,
    val mode: MECCGReleasedOptionMode
) : SpinnerOption {
    override val displayName: String
        get() = name
}

enum class MECCGReleasedOptionMode {
    ALL_CARDS,
    ONLY_RELEASED,
    ONLY_UNRELEASED,
}