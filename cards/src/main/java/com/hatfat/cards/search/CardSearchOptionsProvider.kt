package com.hatfat.cards.search

import com.hatfat.cards.search.filter.BasicTextSearchFilter

interface CardSearchOptionsProvider {
    fun getTextSearchOptions(): List<BasicTextSearchFilter>
}