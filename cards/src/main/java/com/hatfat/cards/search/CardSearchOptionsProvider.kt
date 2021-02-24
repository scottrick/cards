package com.hatfat.cards.search

interface CardSearchOptionsProvider {
    fun getTextSearchOptions(): List<TextSearchOption>
}