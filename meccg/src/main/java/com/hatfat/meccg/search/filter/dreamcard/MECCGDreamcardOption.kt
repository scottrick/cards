package com.hatfat.meccg.search.filter.dreamcard

import com.hatfat.cards.search.filter.SpinnerOption

data class MECCGDreamcardOption(
    val name: String,
    val mode: MECCGDreamcardOptionMode
) : SpinnerOption {
    override val displayName: String
        get() = name
}

enum class MECCGDreamcardOptionMode {
    ALL_CARDS,
    NO_DREAMCARDS,
    ONLY_DREAMCARDS,
}