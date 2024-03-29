package com.hatfat.swccg.search

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
import com.hatfat.swccg.R
import com.hatfat.swccg.data.format.SWCCGFormat
import com.hatfat.swccg.repo.SWCCGFormatRepository
import com.hatfat.swccg.repo.SWCCGMetaDataRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import com.hatfat.swccg.search.filter.advanced.SWCCGAdvancedFilter
import com.hatfat.swccg.search.filter.advanced.SWCCGAdvancedFilterField
import com.hatfat.swccg.search.filter.advanced.SWCCGField
import com.hatfat.swccg.search.filter.format.SWCCGFormatFilter
import com.hatfat.swccg.search.filter.format.SWCCGFormatOption
import com.hatfat.swccg.search.filter.set.SWCCGSetFilter
import com.hatfat.swccg.search.filter.set.SWCCGSetOption
import com.hatfat.swccg.search.filter.side.SWCCGSideFilter
import com.hatfat.swccg.search.filter.side.SWCCGSideOption
import com.hatfat.swccg.search.filter.text.SWCCGTextFilterMode
import com.hatfat.swccg.search.filter.type.SWCCGTypeFilter
import com.hatfat.swccg.search.filter.type.SWCCGTypeOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SWCCGCardSearchOptionsProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val metaDataRepository: SWCCGMetaDataRepository,
    private val setRepository: SWCCGSetRepository,
    private val formatRepository: SWCCGFormatRepository
) : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextFilter> {
        return listOf(
            TextFilter(
                SWCCGTextFilterMode.TITLE_ABBR.toString(),
                SWCCGTextFilterMode.TITLE_ABBR,
                context.getString(R.string.text_search_option_title_abbr),
                true
            ),
            TextFilter(
                SWCCGTextFilterMode.GAMETEXT.toString(),
                SWCCGTextFilterMode.GAMETEXT,
                context.getString(R.string.text_search_option_gametext),
                false
            ),
            TextFilter(
                SWCCGTextFilterMode.LORE.toString(),
                SWCCGTextFilterMode.LORE,
                context.getString(R.string.text_search_option_lore),
                false
            )
        )
    }

    override fun getDropdownFilterLiveData(savedStateHandle: SavedStateHandle): List<MutableLiveData<SpinnerFilter>> {
        return listOf(
            sideLiveData(savedStateHandle),
            typeLiveData(savedStateHandle),
            setLiveData(savedStateHandle),
            formatLiveData(savedStateHandle)
        )
    }

    override fun hasAdvancedFilters(): Boolean {
        return true
    }

    override fun getNewAdvancedFilter(): AdvancedFilter {
        return SWCCGAdvancedFilter(
            SWCCGField.entries.map { SWCCGAdvancedFilterField(it) }.sortedBy { it.displayName },
            AdvancedFilterMode.entries.toList()
        )
    }

    private fun sideLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(SWCCGSideOption(context.getString(R.string.swccg_any_side)))
        val defaultValue = SWCCGSideFilter(
            initialList,
            initialList[0]
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "sideKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            metaDataRepository.sides.value?.takeIf { it.isNotEmpty() }?.let { sides ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = sides.map { SWCCGSideOption(it) }.sortedBy { it.displayName }
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
        mediatorLiveData.addSource(metaDataRepository.sides, onChangedListener)

        return mediatorLiveData
    }

    private fun typeLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(SWCCGTypeOption(context.getString(R.string.swccg_any_type)))
        val defaultValue = SWCCGTypeFilter(
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

                val options = types.map { SWCCGTypeOption(it) }.sortedBy { it.displayName }
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
        val initialList = listOf(SWCCGSetOption(context.getString(R.string.swccg_any_set), "0"))
        val defaultValue = SWCCGSetFilter(
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
            setRepository.setList.value?.filter { !it.legacy }?.takeIf {
                it.isNotEmpty()
            }?.let { sets ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = sets.map {
                    var name = it.gempName ?: it.name
                    if (name.length > 20 && it.abbr != null) {
                        name = it.abbr
                    }
                    SWCCGSetOption(name, it.id)
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

    private fun formatLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(
            SWCCGFormatOption(
                SWCCGFormat(
                    context.getString(R.string.swccg_non_legacy),
                    "non_legacy",
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    listOf("601"),
                    emptyList(),
                )
            ),
            SWCCGFormatOption(
                SWCCGFormat(
                    context.getString(R.string.swccg_any_format),
                    "any_format",
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                )
            )
        )
        val defaultValue = SWCCGFormatFilter(
            initialList,
            initialList[1],
            initialList[0],
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "formatKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            formatRepository.formatsList.value?.takeIf {
                it.isNotEmpty()
            }?.let { formats ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = formats.map { SWCCGFormatOption(it) }

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
        mediatorLiveData.addSource(formatRepository.formatsList, onChangedListener)

        return mediatorLiveData
    }
}