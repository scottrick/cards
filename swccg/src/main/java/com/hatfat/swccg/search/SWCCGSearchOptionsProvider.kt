package com.hatfat.swccg.search

import com.hatfat.cards.search.SearchOptionsProvider
import com.hatfat.cards.search.TextSearchOption
import com.hatfat.swccg.R
import javax.inject.Inject

class SWCCGSearchOptionsProvider @Inject constructor() : SearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextSearchOption> {
        return listOf(
            TextSearchOption(SWCCGSearchOptions.TITLE, R.string.text_search_option_title, true),
            TextSearchOption(SWCCGSearchOptions.GAMETEXT, R.string.text_search_option_gametext),
            TextSearchOption(SWCCGSearchOptions.LORE, R.string.text_search_option_lore)
        )
    }
}