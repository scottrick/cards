package com.hatfat.trek1.search.filter.affil

import com.hatfat.cards.search.filter.SpinnerOption

data class Trek1AffiliationOption(
    val name: String
) : SpinnerOption {
    override val displayName: String
        get() = name
}