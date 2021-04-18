package com.hatfat.trek2.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilterMode
import com.hatfat.trek2.search.filter.Trek2Filter
import com.hatfat.trek2.data.Trek2Card
import java.io.Serializable

class Trek2AdvancedFilter(
    override val fields: List<Trek2AdvancedFilterField>,
    override val modes: List<AdvancedFilterMode>
) : AdvancedFilter(
    fields,
    modes
), Trek2Filter, Serializable {
    override fun filter(card: Trek2Card): Boolean {
        return true
//        val field = fields[selectedFieldIndex]
//        field.getFieldValuesForCard(card, setRepository).forEach {
//            if (fieldFilter(it)) {
//                return true
//            }
//        }
//        return false
    }
}