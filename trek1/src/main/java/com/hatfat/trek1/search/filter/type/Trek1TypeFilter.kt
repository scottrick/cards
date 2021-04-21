package com.hatfat.trek1.search.filter.type

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.filter.Trek1Filter
import java.io.Serializable

class Trek1TypeFilter(
    options: List<Trek1TypeOption>,
    notSelectedOption: Trek1TypeOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), Trek1Filter, Serializable {
    override fun filter(card: Trek1Card, setRepository: Trek1SetRepository): Boolean {
        return card.type?.contains((selectedOption as Trek1TypeOption).name) == true
    }
}