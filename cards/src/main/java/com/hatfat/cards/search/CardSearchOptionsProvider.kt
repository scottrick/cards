package com.hatfat.cards.search

import com.hatfat.cards.search.param.BasicTextSearchParam

interface CardSearchOptionsProvider {
    fun getTextSearchOptions(): List<BasicTextSearchParam>
}