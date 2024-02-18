package com.hatfat.trek2.search

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
import com.hatfat.trek2.R
import com.hatfat.trek2.data.Trek2Affiliation
import com.hatfat.trek2.repo.Trek2MetaDataRepository
import com.hatfat.trek2.repo.Trek2SetRepository
import com.hatfat.trek2.search.filter.advanced.Trek2AdvancedFilter
import com.hatfat.trek2.search.filter.advanced.Trek2AdvancedFilterField
import com.hatfat.trek2.search.filter.advanced.Trek2Field
import com.hatfat.trek2.search.filter.affil.Trek2AffiliationFilter
import com.hatfat.trek2.search.filter.affil.Trek2AffiliationOption
import com.hatfat.trek2.search.filter.format.Trek2FormatFilter
import com.hatfat.trek2.search.filter.format.Trek2FormatOption
import com.hatfat.trek2.search.filter.format.Trek2FormatOptionType
import com.hatfat.trek2.search.filter.set.Trek2SetFilter
import com.hatfat.trek2.search.filter.set.Trek2SetOption
import com.hatfat.trek2.search.filter.text.Trek2TextFilterMode
import com.hatfat.trek2.search.filter.type.Trek2TypeFilter
import com.hatfat.trek2.search.filter.type.Trek2TypeOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Trek2CardSearchOptionsProvider
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val metaDataRepository: Trek2MetaDataRepository,
    private val setRepository: Trek2SetRepository
) : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextFilter> {
        return listOf(
            TextFilter(
                Trek2TextFilterMode.TITLE.toString(),
                Trek2TextFilterMode.TITLE,
                context.getString(R.string.text_search_option_title),
                true
            ),
            TextFilter(
                Trek2TextFilterMode.TEXT.toString(),
                Trek2TextFilterMode.TEXT,
                context.getString(R.string.text_search_option_text),
                false
            ),
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
        val initialList =
            listOf(
                Trek2AffiliationOption(
                    Trek2Affiliation(
                        context.getString(R.string.trek2_any_affiliation),
                        emptyList()
                    )
                )
            )
        val defaultValue = Trek2AffiliationFilter(
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
            metaDataRepository.affiliations.value?.takeIf { it.isNotEmpty() }?.let { affiliations ->
                val persistedData = persistedLiveData.value ?: defaultValue
                val newOptions = initialList.toMutableList()

                val options = affiliations.values.toSet().map { Trek2AffiliationOption(it) }
                    .sortedBy { it.displayName }
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
        mediatorLiveData.addSource(metaDataRepository.affiliations, onChangedListener)

        return mediatorLiveData
    }

    private fun typeLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(Trek2TypeOption(context.getString(R.string.trek2_any_type)))
        val defaultValue = Trek2TypeFilter(
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

                val options = types.map { Trek2TypeOption(it) }.sortedBy { it.displayName }
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
        val initialList = listOf(Trek2SetOption(context.getString(R.string.trek2_any_set), "0"))
        val defaultValue = Trek2SetFilter(
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
                    Trek2SetOption(it.name, it.code)
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
            Trek2FormatOption(
                context.getString(R.string.trek2_format_any),
                Trek2FormatOptionType.ANY
            ),
            Trek2FormatOption(
                context.getString(R.string.trek2_format_non_virtual_only),
                Trek2FormatOptionType.NO_VIRTUAL
            ),
            Trek2FormatOption(
                context.getString(R.string.trek2_format_virtual_only),
                Trek2FormatOptionType.ONLY_VIRTUAL
            ),
        )

        val defaultValue = Trek2FormatFilter(
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
        return Trek2AdvancedFilter(
            Trek2Field.entries.map { Trek2AdvancedFilterField(it) }.sortedBy { it.displayName },
            AdvancedFilterMode.entries.toList()
        )
    }
}