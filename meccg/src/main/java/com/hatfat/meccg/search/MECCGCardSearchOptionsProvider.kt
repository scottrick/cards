package com.hatfat.meccg.search

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.TextFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilter
import com.hatfat.meccg.R
import com.hatfat.meccg.repo.MECCGMetaDataRepository
import com.hatfat.meccg.search.filter.alignment.MECCGAlignmentFilter
import com.hatfat.meccg.search.filter.alignment.MECCGAlignmentOption
import com.hatfat.meccg.search.filter.text.MECCGTextFilterMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MECCGCardSearchOptionsProvider
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val metaDataRepository: MECCGMetaDataRepository
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
        return listOf(
            alignmentLiveData(savedStateHandle)
        )
    }

    private fun alignmentLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(MECCGAlignmentOption("Any Alignment"))
        val defaultValue = MECCGAlignmentFilter(
            initialList,
            initialList[0]
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "alignmentKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            metaDataRepository.cardAlignments.value?.takeIf { it.isNotEmpty() }?.let { alignments ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = alignments.map { MECCGAlignmentOption(it) }.sortedBy { it.displayName }
                newOptions.addAll(options)

                if (newOptions != persistedData.options) {
                    persistedData.options = newOptions

                    if (!newOptions.contains(persistedData.selectedOption)) {
                        persistedData.selectedOption = newOptions[0]
                    }

                    persistedLiveData.value = persistedData
                    mediatorLiveData.value = persistedData
                }
            }
        }

        mediatorLiveData.addSource(persistedLiveData, onChangedListener)
        mediatorLiveData.addSource(metaDataRepository.cardAlignments, onChangedListener)

        return mediatorLiveData
    }

    override fun hasAdvancedFilters(): Boolean {
        return false
    }

    override fun getNewAdvancedFilter(): AdvancedFilter? {
        return null
    }
}