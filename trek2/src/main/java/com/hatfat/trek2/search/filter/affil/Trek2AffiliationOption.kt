package com.hatfat.trek2.search.filter.affil

import com.hatfat.cards.search.filter.SpinnerOption
import com.hatfat.trek2.data.Trek2Affiliation

data class Trek2AffiliationOption(
    val affiliation: Trek2Affiliation
) : SpinnerOption {
    override val displayName: String
        get() = affiliation.displayName
}