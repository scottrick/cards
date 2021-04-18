package com.hatfat.trek2.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilterField
import com.hatfat.trek2.data.Trek2Card
import java.io.Serializable

data class Trek2AdvancedFilterField(
    val trek2Field: Trek2Field
) : AdvancedFilterField, Serializable {

    override val displayName: String
        get() = trek2Field.displayName

    fun getFieldValuesForCard(card: Trek2Card): List<String> {
        return mutableListOf<String>().apply {
        }
    }
}