package com.hatfat.trek1.search

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
import com.hatfat.trek1.R
import com.hatfat.trek1.repo.Trek1MetaDataRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import com.hatfat.trek1.search.filter.advanced.Trek1AdvancedFilter
import com.hatfat.trek1.search.filter.advanced.Trek1AdvancedFilterField
import com.hatfat.trek1.search.filter.advanced.Trek1Field
import com.hatfat.trek1.search.filter.affil.Trek1AffiliationFilter
import com.hatfat.trek1.search.filter.affil.Trek1AffiliationOption
import com.hatfat.trek1.search.filter.format.Trek1FormatFilter
import com.hatfat.trek1.search.filter.format.Trek1FormatOption
import com.hatfat.trek1.search.filter.format.Trek1FormatOptionType
import com.hatfat.trek1.search.filter.set.Trek1SetFilter
import com.hatfat.trek1.search.filter.set.Trek1SetOption
import com.hatfat.trek1.search.filter.text.Trek1TextFilterMode
import com.hatfat.trek1.search.filter.type.Trek1TypeFilter
import com.hatfat.trek1.search.filter.type.Trek1TypeOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Trek1CardSearchOptionsProvider
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val metaDataRepository: Trek1MetaDataRepository,
    private val setRepository: Trek1SetRepository
) : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextFilter> {
        return listOf(
            TextFilter(Trek1TextFilterMode.TITLE.toString(), Trek1TextFilterMode.TITLE, context.getString(R.string.text_search_option_title), true),
            TextFilter(Trek1TextFilterMode.GAMETEXT.toString(), Trek1TextFilterMode.GAMETEXT, context.getString(R.string.text_search_option_gametext), false),
            TextFilter(Trek1TextFilterMode.LORE.toString(), Trek1TextFilterMode.LORE, context.getString(R.string.text_search_option_lore), false)
        )
    }

    override fun getDropdownFilterLiveData(savedStateHandle: SavedStateHandle): List<MutableLiveData<SpinnerFilter>> {
        return listOf(
            affiliationsLiveData(savedStateHandle),
            typeLiveData(savedStateHandle),
            setLiveData(savedStateHandle),
            formatLivedata(savedStateHandle)
        )
    }

    private fun affiliationsLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(Trek1AffiliationOption("Any Affiliation", emptyList()))
        val defaultValue = Trek1AffiliationFilter(
            initialList,
            initialList[0]
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "affiliationKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            metaDataRepository.affiliationOptions.value?.takeIf { it.isNotEmpty() }?.let { affiliationOptions ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = affiliationOptions.toTypedArray()
                options.sort()
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
        mediatorLiveData.addSource(metaDataRepository.affiliationOptions, onChangedListener)

        return mediatorLiveData
    }

    private fun typeLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(Trek1TypeOption("Any Type"))
        val defaultValue = Trek1TypeFilter(
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
            metaDataRepository.types.value?.takeIf { it.isNotEmpty() }?.let { types ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = types.map { Trek1TypeOption(it) }.sortedBy { it.displayName }
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
        mediatorLiveData.addSource(metaDataRepository.types, onChangedListener)

        return mediatorLiveData
    }

    private fun setLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(Trek1SetOption("Any Set", "0"))
        val defaultValue = Trek1SetFilter(
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
                    Trek1SetOption(it.name, it.code)
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

    private fun formatLivedata(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(
            Trek1FormatOption("Any Format", Trek1FormatOptionType.ANY),
            Trek1FormatOption("Non-Virtual Only", Trek1FormatOptionType.NO_VIRTUAL),
            Trek1FormatOption("Virtual Only", Trek1FormatOptionType.ONLY_VIRTUAL),
            Trek1FormatOption("PAQ", Trek1FormatOptionType.PAQ)
        )

        val defaultValue = Trek1FormatFilter(
            initialList,
            initialList[0]
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "formatKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        return mediatorLiveData
    }

    override fun hasAdvancedFilters(): Boolean {
        return true
    }

    override fun getNewAdvancedFilter(): AdvancedFilter {
        return Trek1AdvancedFilter(
            Trek1Field.values().map { Trek1AdvancedFilterField(it) }.sortedBy { it.displayName },
            AdvancedFilterMode.values().toList()
        )
    }
}