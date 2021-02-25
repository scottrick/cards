package com.hatfat.swccg.search

import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.cards.search.filter.BasicTextSearchFilter
import com.hatfat.swccg.R
import javax.inject.Inject

class SWCCGCardSearchOptionsProvider @Inject constructor() : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<BasicTextSearchFilter> {
        return listOf(
            BasicTextSearchFilter(SWCCGSearchOptions.TITLE, R.string.text_search_option_title, true),
            BasicTextSearchFilter(SWCCGSearchOptions.GAMETEXT, R.string.text_search_option_gametext),
            BasicTextSearchFilter(SWCCGSearchOptions.LORE, R.string.text_search_option_lore)
        )
    }
}