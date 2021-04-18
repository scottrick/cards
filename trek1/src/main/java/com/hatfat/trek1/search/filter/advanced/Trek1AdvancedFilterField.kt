package com.hatfat.trek1.search.filter.advanced

import com.hatfat.cards.search.filter.advanced.AdvancedFilterField
import com.hatfat.trek1.data.Trek1Card
import java.io.Serializable

data class Trek1AdvancedFilterField(
    val trek1Field: Trek1Field
) : AdvancedFilterField, Serializable {

    override val displayName: String
        get() = trek1Field.displayName

    fun getFieldValuesForCard(card: Trek1Card): List<String> {
        return mutableListOf<String>().apply {
        }
    }
}