package com.hatfat.meccg.search

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.TextFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilter
import com.hatfat.meccg.R
import com.hatfat.meccg.search.filter.text.MECCGTextFilterMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MECCGCardSearchOptionsProvider
@Inject constructor(
    @ApplicationContext private val context: Context
//        private val metaDataRepository: SWCCGMetaDataRepository,
//        private val setRepository: SWCCGSetRepository,
//        private val formatRepository: SWCCGFormatRepository
) : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextFilter> {
        return listOf(
            TextFilter(MECCGTextFilterMode.TITLE.toString(), MECCGTextFilterMode.TITLE, context.getString(R.string.text_search_option_title), true),
            TextFilter(MECCGTextFilterMode.TEXT.toString(), MECCGTextFilterMode.TEXT, context.getString(R.string.text_search_option_text), false),
        )
    }

    override fun getDropdownFilterLiveData(savedStateHandle: SavedStateHandle): List<MutableLiveData<SpinnerFilter>> {
        return emptyList()
    }

    override fun hasAdvancedFilters(): Boolean {
        return false
//        return true
    }

    override fun getNewAdvancedFilter(): AdvancedFilter? {
        return null
    }
}