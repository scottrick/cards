package com.hatfat.trek1.search.filter.affil

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.filter.Trek1Filter
import java.io.Serializable

class Trek1AffiliationFilter(
    options: List<Trek1AffiliationOption>,
    notSelectedOption: Trek1AffiliationOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), Trek1Filter, Serializable {
    override fun filter(card: Trek1Card, setRepository: Trek1SetRepository): Boolean {
        return card.affiliation?.contains((selectedOption as Trek1AffiliationOption).name) == true
    }
}