package com.hatfat.trek2.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilterMode
import com.hatfat.trek2.data.Trek2Card
import com.hatfat.trek2.repo.Trek2SetRepository
import com.hatfat.trek2.search.filter.Trek2Filter
import java.io.Serializable

class Trek2AdvancedFilter(
    override val fields: List<Trek2AdvancedFilterField>,
    override val modes: List<AdvancedFilterMode>
) : AdvancedFilter(
    fields,
    modes
), Trek2Filter, Serializable {
    override fun filter(card: Trek2Card, setRepository: Trek2SetRepository): Boolean {
        val field = fields[selectedFieldIndex]
        val fieldValues = field.getFieldValuesForCard(card, setRepository)

        if (fieldValues.isEmpty()) {
            return false
        }

        return fieldsFilter(fieldValues)
    }
}