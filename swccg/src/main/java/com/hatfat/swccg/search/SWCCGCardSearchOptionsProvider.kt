package com.hatfat.swccg.search

import android.content.Context
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.TextFilter
import com.hatfat.swccg.R
import com.hatfat.swccg.search.filter.SWCCGSideFilter
import com.hatfat.swccg.search.filter.SWCCGTextFilterMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SWCCGCardSearchOptionsProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextFilter> {
        return listOf(
            TextFilter(SWCCGTextFilterMode.TITLE.toString(), SWCCGTextFilterMode.TITLE, context.getString(R.string.text_search_option_title), true),
            TextFilter(SWCCGTextFilterMode.GAMETEXT.toString(), SWCCGTextFilterMode.GAMETEXT, context.getString(R.string.text_search_option_gametext), false),
            TextFilter(SWCCGTextFilterMode.LORE.toString(), SWCCGTextFilterMode.LORE, context.getString(R.string.text_search_option_lore), false)
        )
    }

    override fun getDropdownFilterOptions(): List<SpinnerFilter> {
        return listOf(
            SWCCGSideFilter(),
            SWCCGSideFilter(),
            SWCCGSideFilter(),
            SWCCGSideFilter(),
            SWCCGSideFilter()
        )
    }
}