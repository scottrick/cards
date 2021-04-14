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
import com.hatfat.cards.search.filter.advanced.AdvancedFilterMode
import com.hatfat.meccg.R
import com.hatfat.meccg.repo.MECCGMetaDataRepository
import com.hatfat.meccg.repo.MECCGSetRepository
import com.hatfat.meccg.search.filter.advanced.MECCGAdvancedFilter
import com.hatfat.meccg.search.filter.advanced.MECCGAdvancedFilterField
import com.hatfat.meccg.search.filter.advanced.MECCGField
import com.hatfat.meccg.search.filter.alignment.MECCGAlignmentFilter
import com.hatfat.meccg.search.filter.alignment.MECCGAlignmentOption
import com.hatfat.meccg.search.filter.key.MECCGKeyFilter
import com.hatfat.meccg.search.filter.key.MECCGKeyOption
import com.hatfat.meccg.search.filter.set.MECCGSetFilter
import com.hatfat.meccg.search.filter.set.MECCGSetOption
import com.hatfat.meccg.search.filter.text.MECCGTextFilterMode
import com.hatfat.meccg.search.filter.type.MECCGTypeFilter
import com.hatfat.meccg.search.filter.type.MECCGTypeOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MECCGCardSearchOptionsProvider
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val metaDataRepository: MECCGMetaDataRepository,
    private val setRepository: MECCGSetRepository
) : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextFilter> {
        return listOf(
            TextFilter(MECCGTextFilterMode.TITLE.toString(), MECCGTextFilterMode.TITLE, context.getString(R.string.text_search_option_title), true),
            TextFilter(MECCGTextFilterMode.TEXT.toString(), MECCGTextFilterMode.TEXT, context.getString(R.string.text_search_option_text), false),
        )
    }

    override fun getDropdownFilterLiveData(savedStateHandle: SavedStateHandle): List<MutableLiveData<SpinnerFilter>> {
        return listOf(
            alignmentLiveData(savedStateHandle),
            typeLiveData(savedStateHandle),
            setLiveData(savedStateHandle),
            keyLiveData(savedStateHandle)
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

    private fun typeLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(MECCGTypeOption("Any Type"))
        val defaultValue = MECCGTypeFilter(
            initialList,
            initialList[0]
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "typeKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            metaDataRepository.cardTypes.value?.takeIf { it.isNotEmpty() }?.let { types ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = types.map { MECCGTypeOption(it) }.sortedBy { it.displayName }
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
        mediatorLiveData.addSource(metaDataRepository.cardTypes, onChangedListener)

        return mediatorLiveData
    }

    private fun setLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(MECCGSetOption("Any Set", "0"))
        val defaultValue = MECCGSetFilter(
            initialList,
            initialList[0]
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "setKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            setRepository.setList.value?.takeIf {
                it.isNotEmpty()
            }?.let { sets ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = sets.map {
                    MECCGSetOption(it.name ?: "Unknown", it.code ?: "")
                }

                options.let {
                    newOptions.addAll((it))
                }

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
        mediatorLiveData.addSource(setRepository.setList, onChangedListener)

        return mediatorLiveData
    }

    private fun keyLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(MECCGKeyOption("Any Key"))
        val defaultValue = MECCGKeyFilter(
            initialList,
            initialList[0]
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "keyKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            metaDataRepository.keys.value?.takeIf { it.isNotEmpty() }?.let { keys ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = keys.map { MECCGKeyOption(it) }.sortedBy { it.displayName }
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
        mediatorLiveData.addSource(metaDataRepository.keys, onChangedListener)

        return mediatorLiveData
    }

    override fun hasAdvancedFilters(): Boolean {
        return true
    }

    override fun getNewAdvancedFilter(): AdvancedFilter {
        return MECCGAdvancedFilter(
            MECCGField.values().map { MECCGAdvancedFilterField(it) }.sortedBy { it.displayName },
            AdvancedFilterMode.values().toList()
        )
    }
}