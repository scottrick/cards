package com.hatfat.swccg.search.filter.format

import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.search.filter.SWCCGFilter
import java.io.Serializable
import java.util.*

class SWCCGFormatFilter(
    options: List<SWCCGFormatOption>,
    notSelectedOption: SWCCGFormatOption?
) : SpinnerFilter(
    options,
    notSelectedOption
), SWCCGFilter, Serializable {
    override fun filter(card: SWCCGCard): Boolean {
        val format = (selectedOption as SWCCGFormatOption).format

        if (format.banned?.isNotEmpty() == true) {
            /* we have banned cards */
            if (format.banned.contains(card.gempId)) {
                return false
            }
        }

        if (format.bannedRarities?.isNotEmpty() == true) {
            /* we have banned rarities */
            if (format.bannedRarities.contains(card.rarity)) {
                return false
            }
        }

        if (format.bannedIcons?.isNotEmpty() == true) {
            /* we have banned icons */
            val icons = mutableListOf<String>()
            card.front.icons?.let {
                icons.addAll(it)
            }
            card.back?.icons?.let {
                icons.addAll(it)
            }

            val intersect = icons.map { it.toUpperCase(Locale.getDefault()).replace(" ", "_") }.intersect(format.bannedIcons)
            if (intersect.isNotEmpty()) {
                return false
            }
        }

        if (format.set?.isNotEmpty() == true) {
            /* we have a list of legal sets */
            val intersect = card.printings?.map { it.set }?.intersect(format.set)
            return intersect?.size ?: 0 > 0
        }

        return true
    }
}