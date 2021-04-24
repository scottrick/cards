package com.hatfat.trek2.search.filter.affil

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.repo.Trek2SetRepository
import com.hatfat.trek2.search.filter.Trek2Filter
import java.io.Serializable

class Trek2AffiliationFilter(
    options: List<Trek2AffiliationOption>,
    notSelectedOption: Trek2AffiliationOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), Trek2Filter, Serializable {
    override fun filter(card: Trek2Card, setRepository: Trek2SetRepository): Boolean {
        if (card.type == "Mission") {
            (selectedOption as Trek2AffiliationOption).affiliation.missionAbbrs.forEach { abbr ->
                if (card.affiliation?.contains(abbr) == true) {
                    return true
                }
            }

            return false
        } else {
            return card.affiliation?.contains((selectedOption as Trek2AffiliationOption).displayName) == true
        }
    }
}