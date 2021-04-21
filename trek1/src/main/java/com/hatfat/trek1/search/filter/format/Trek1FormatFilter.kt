package com.hatfat.trek1.search.filter.format

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.filter.Trek1Filter
import java.io.Serializable

class Trek1FormatFilter(
    options: List<Trek1FormatOption>,
    notSelectedOption: Trek1FormatOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), Trek1Filter, Serializable {
    override fun filter(card: Trek1Card, setRepository: Trek1SetRepository): Boolean {
        return (selectedOption as Trek1FormatOption).isVirtual == card.virtual
    }
}