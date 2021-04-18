package com.hatfat.trek2.search

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.hatfat.cards.search.CardSearchOptionsProvider
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.TextFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilter
import com.hatfat.trek2.R
import com.hatfat.trek2.search.filter.text.Trek2TextFilterMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Trek2CardSearchOptionsProvider
@Inject constructor(
    @ApplicationContext private val context: Context
//    private val metaDataRepository: MECCGMetaDataRepository,
//    private val setRepository: MECCGSetRepository
) : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextFilter> {
        return listOf(
            TextFilter(Trek2TextFilterMode.TITLE.toString(), Trek2TextFilterMode.TITLE, context.getString(R.string.text_search_option_title), true),
            TextFilter(Trek2TextFilterMode.TEXT.toString(), Trek2TextFilterMode.TEXT, context.getString(R.string.text_search_option_text), false),
        )
    }

    override fun getDropdownFilterLiveData(savedStateHandle: SavedStateHandle): List<MutableLiveData<SpinnerFilter>> {
        return listOf(
//            alignmentLiveData(savedStateHandle),
//            typeLiveData(savedStateHandle),
//            setLiveData(savedStateHandle),
//            keyLiveData(savedStateHandle)
        )
    }

//    private fun alignmentLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
//        val initialList = listOf(Trek2AlignmentOption("Any Alignment"))
//        val defaultValue = Trek2AlignmentFilter(
//            initialList,
//            initialList[0]
//        )
//
//        val persistedLiveData = savedStateHandle.getLiveData(
//            "alignmentKey",
//            defaultValue
//        )
//
//        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
//        mediatorLiveData.value = persistedLiveData.value
//
//        val onChangedListener = Observer<Any> {
//            metaDataRepository.cardAlignments.value?.takeIf { it.isNotEmpty() }?.let { alignments ->
//                val persistedData = persistedLiveData.value ?: defaultValue
//                val newOptions = initialList.toMutableList()
//
//                val options = alignments.map { Trek2AlignmentOption(it) }.sortedBy { it.displayName }
//                newOptions.addAll(options)
//
//                if (newOptions != persistedData.options) {
//                    persistedData.options = newOptions
//
//                    if (!newOptions.contains(persistedData.selectedOption)) {
//                        persistedData.selectedOption = newOptions[0]
//                    }
//
//                    persistedLiveData.value = persistedData
//                    mediatorLiveData.value = persistedData
//                }
//            }
//        }
//
//        mediatorLiveData.addSource(persistedLiveData, onChangedListener)
//        mediatorLiveData.addSource(metaDataRepository.cardAlignments, onChangedListener)
//
//        return mediatorLiveData
//    }
//
//    private fun typeLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
//        val initialList = listOf(Trek2TypeOption("Any Type"))
//        val defaultValue = Trek2TypeFilter(
//            initialList,
//            initialList[0]
//        )
//
//        val persistedLiveData = savedStateHandle.getLiveData(
//            "typeKey",
//            defaultValue
//        )
//
//        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
//        mediatorLiveData.value = persistedLiveData.value
//
//        val onChangedListener = Observer<Any> {
//            metaDataRepository.cardTypes.value?.takeIf { it.isNotEmpty() }?.let { types ->
//                val persistedData = persistedLiveData.value ?: defaultValue
//                val newOptions = initialList.toMutableList()
//
//                val options = types.map { Trek2TypeOption(it) }.sortedBy { it.displayName }
//                newOptions.addAll(options)
//
//                if (newOptions != persistedData.options) {
//                    persistedData.options = newOptions
//
//                    if (!newOptions.contains(persistedData.selectedOption)) {
//                        persistedData.selectedOption = newOptions[0]
//                    }
//
//                    persistedLiveData.value = persistedData
//                    mediatorLiveData.value = persistedData
//                }
//            }
//        }
//
//        mediatorLiveData.addSource(persistedLiveData, onChangedListener)
//        mediatorLiveData.addSource(metaDataRepository.cardTypes, onChangedListener)
//
//        return mediatorLiveData
//    }
//
//    private fun setLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
//        val initialList = listOf(Trek2SetOption("Any Set", "0"))
//        val defaultValue = Trek2SetFilter(
//            initialList,
//            initialList[0]
//        )
//
//        val persistedLiveData = savedStateHandle.getLiveData(
//            "setKey",
//            defaultValue
//        )
//
//        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
//        mediatorLiveData.value = persistedLiveData.value
//
//        val onChangedListener = Observer<Any> {
//            setRepository.setList.value?.takeIf {
//                it.isNotEmpty()
//            }?.let { sets ->
//                val persistedData = persistedLiveData.value ?: defaultValue
//                val newOptions = initialList.toMutableList()
//
//                val options = sets.map {
//                    Trek2SetOption(it.name ?: "Unknown", it.code ?: "")
//                }
//
//                options.let {
//                    newOptions.addAll((it))
//                }
//
//                if (newOptions != persistedData.options) {
//                    persistedData.options = newOptions
//
//                    if (!newOptions.contains(persistedData.selectedOption)) {
//                        persistedData.selectedOption = newOptions[0]
//                    }
//
//                    persistedLiveData.value = persistedData
//                    mediatorLiveData.value = persistedData
//                }
//            }
//        }
//
//        mediatorLiveData.addSource(persistedLiveData, onChangedListener)
//        mediatorLiveData.addSource(setRepository.setList, onChangedListener)
//
//        return mediatorLiveData
//    }
//
//    private fun keyLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
//        val initialList = listOf(MECCGKeyOption("Any Key"))
//        val defaultValue = MECCGKeyFilter(
//            initialList,
//            initialList[0]
//        )
//
//        val persistedLiveData = savedStateHandle.getLiveData(
//            "keyKey",
//            defaultValue
//        )
//
//        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
//        mediatorLiveData.value = persistedLiveData.value
//
//        val onChangedListener = Observer<Any> {
//            metaDataRepository.keys.value?.takeIf { it.isNotEmpty() }?.let { keys ->
//                val persistedData = persistedLiveData.value ?: defaultValue
//                val newOptions = initialList.toMutableList()
//
//                val options = keys.map { MECCGKeyOption(it) }.sortedBy { it.displayName }
//                newOptions.addAll(options)
//
//                if (newOptions != persistedData.options) {
//                    persistedData.options = newOptions
//
//                    if (!newOptions.contains(persistedData.selectedOption)) {
//                        persistedData.selectedOption = newOptions[0]
//                    }
//
//                    persistedLiveData.value = persistedData
//                    mediatorLiveData.value = persistedData
//                }
//            }
//        }
//
//        mediatorLiveData.addSource(persistedLiveData, onChangedListener)
//        mediatorLiveData.addSource(metaDataRepository.keys, onChangedListener)
//
//        return mediatorLiveData
//    }

    override fun hasAdvancedFilters(): Boolean {
        return false
    }

    override fun getNewAdvancedFilter(): AdvancedFilter? {
        return null
//        return Trek2AdvancedFilter(
//            Trek2Field.values().map { Trek2AdvancedFilterField(it) }.sortedBy { it.displayName },
//            AdvancedFilterMode.values().toList()
//        )
    }
}