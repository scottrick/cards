package com.hatfat.swccg.search

import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.cards.search.param.BasicTextSearchParam
import com.hatfat.swccg.R
import javax.inject.Inject

class SWCCGCardSearchOptionsProvider @Inject constructor() : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<BasicTextSearchParam> {
        return listOf(
            BasicTextSearchParam(SWCCGStringSearchOptions.TITLE, R.string.text_search_option_title, true),
            BasicTextSearchParam(SWCCGStringSearchOptions.GAMETEXT, R.string.text_search_option_gametext, false),
            BasicTextSearchParam(SWCCGStringSearchOptions.LORE, R.string.text_search_option_lore, false)
        )
    }
}