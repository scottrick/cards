package com.hatfat.cards.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.TextFilter

interface CardSearchOptionsProvider {
    fun getTextSearchOptions(): List<TextFilter>

    fun getDropdownFilterLiveData(savedStateHandle: SavedStateHandle): List<MutableLiveData<SpinnerFilter>>
}
