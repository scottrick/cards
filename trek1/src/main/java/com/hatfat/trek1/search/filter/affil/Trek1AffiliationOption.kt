package com.hatfat.trek1.search.filter.affil

import com.hatfat.cards.search.filter.SpinnerOption
import com.hatfat.trek1.data.Trek1Affiliation

data class Trek1AffiliationOption(
    val name: String,
    val affiliations: List<Trek1Affiliation>
) : SpinnerOption, Comparable<Trek1AffiliationOption> {
    override val displayName: String
        get() = name

    override fun compareTo(other: Trek1AffiliationOption): Int {
        if (affiliations.size == 1 && other.affiliations.size > 1) {
            return -1
        }
        if (affiliations.size > 1 && other.affiliations.size == 1) {
            return 1
        }

        if (displayName.isBlank() && other.displayName.isBlank()) {
            return 0
        }

        if (displayName.isBlank()) {
            return -1
        }

        if (other.displayName.isBlank()) {
            return 1
        }

        return displayName.compareTo(other.displayName)
    }
}