package com.hatfat.cards.search

interface SearchOptionsProvider {
    fun getTextSearchOptions(): List<TextSearchOption>
}