package com.hatfat.swccg.search

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.TextFilter
import com.hatfat.swccg.R
import com.hatfat.swccg.repo.SWCCGMetaDataRepository
import com.hatfat.swccg.search.filter.SWCCGSideFilter
import com.hatfat.swccg.search.filter.SWCCGTextFilterMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SWCCGCardSearchOptionsProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val metaDataRepository: SWCCGMetaDataRepository
) : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextFilter> {
        return listOf(
            TextFilter(SWCCGTextFilterMode.TITLE.toString(), SWCCGTextFilterMode.TITLE, context.getString(R.string.text_search_option_title), true),
            TextFilter(SWCCGTextFilterMode.GAMETEXT.toString(), SWCCGTextFilterMode.GAMETEXT, context.getString(R.string.text_search_option_gametext), false),
            TextFilter(SWCCGTextFilterMode.LORE.toString(), SWCCGTextFilterMode.LORE, context.getString(R.string.text_search_option_lore), false)
        )
    }

    override fun getDropdownFilterLiveData(savedStateHandle: SavedStateHandle): List<MutableLiveData<SpinnerFilter>> {
        return listOf(
            sideLiveData(savedStateHandle)
        )
    }

    private fun sideLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf("Any Side")
        val defaultValue = SWCCGSideFilter(
            initialList,
            initialList[0]
        )

        val persistedLiveData = savedStateHandle.getLiveData<SpinnerFilter>(
            "sideKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            metaDataRepository.sides.value?.takeIf { it.isNotEmpty() }?.let {
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()
                newOptions.addAll(it)

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
        mediatorLiveData.addSource(metaDataRepository.sides, onChangedListener)

        return mediatorLiveData
    }
}