package com.hatfat.trek1.search.filter.set

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek1.data.Trek1Card
import com.hatfat.trek1.repo.Trek1MetaDataRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.filter.Trek1Filter
import java.io.Serializable

class Trek1SetFilter(
    options: List<Trek1SetOption>,
    notSelectedOption: Trek1SetOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), Trek1Filter, Serializable {
    override fun filter(card: Trek1Card, setRepository: Trek1SetRepository, metaDataRepository: Trek1MetaDataRepository): Boolean {
        return (selectedOption as Trek1SetOption).code == card.release
    }
}