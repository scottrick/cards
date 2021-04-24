package com.hatfat.trek2.search.filter.set

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.repo.Trek2SetRepository
import com.hatfat.trek2.search.filter.Trek2Filter
import java.io.Serializable

class Trek2SetFilter(
    options: List<Trek2SetOption>,
    notSelectedOption: Trek2SetOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), Trek2Filter, Serializable {
    override fun filter(card: Trek2Card, setRepository: Trek2SetRepository): Boolean {
        return (selectedOption as Trek2SetOption).code == card.set
    }
}