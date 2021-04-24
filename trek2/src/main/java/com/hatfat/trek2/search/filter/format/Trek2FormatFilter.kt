package com.hatfat.trek2.search.filter.format

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.repo.Trek2SetRepository
import com.hatfat.trek2.search.filter.Trek2Filter
import java.io.Serializable

class Trek2FormatFilter(
    options: List<Trek2FormatOption>,
    notSelectedOption: Trek2FormatOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), Trek2Filter, Serializable {
    override fun filter(card: Trek2Card, setRepository: Trek2SetRepository): Boolean {
        return when ((selectedOption as Trek2FormatOption).optionType) {
            Trek2FormatOptionType.ANY -> true
            Trek2FormatOptionType.NO_VIRTUAL -> {
                !(setRepository.setMap.value?.get(card.set)?.virtual ?: false)
            }
            Trek2FormatOptionType.ONLY_VIRTUAL -> {
                (setRepository.setMap.value?.get(card.set)?.virtual ?: false)
            }
        }
    }
}