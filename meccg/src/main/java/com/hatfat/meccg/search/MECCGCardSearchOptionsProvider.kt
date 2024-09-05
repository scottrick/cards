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
import com.hatfat.meccg.search.filter.dreamcard.MECCGDreamcardFilter
import com.hatfat.meccg.search.filter.dreamcard.MECCGDreamcardOption
import com.hatfat.meccg.search.filter.dreamcard.MECCGDreamcardOptionMode
import com.hatfat.meccg.search.filter.key.MECCGKeyFilter
import com.hatfat.meccg.search.filter.key.MECCGKeyOption
import com.hatfat.meccg.search.filter.released.MECCGReleasedFilter
import com.hatfat.meccg.search.filter.released.MECCGReleasedOption
import com.hatfat.meccg.search.filter.released.MECCGReleasedOptionMode
import com.hatfat.meccg.search.filter.set.MECCGSetFilter
import com.hatfat.meccg.search.filter.set.MECCGSetOption
import com.hatfat.meccg.search.filter.text.MECCGTextFilterMode
import com.hatfat.meccg.search.filter.type.MECCGTypeFilter
import com.hatfat.meccg.search.filter.type.MECCGTypeOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

class MECCGCardSearchOptionsProvider
@Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("should use dreamcards") private val shouldUseDreamcards: Boolean,
    private val metaDataRepository: MECCGMetaDataRepository,
    private val setRepository: MECCGSetRepository
) : CardSearchOptionsProvider {
    override fun getTextSearchOptions(): List<TextFilter> {
        return listOf(
            TextFilter(
                MECCGTextFilterMode.TITLE.toString(),
                MECCGTextFilterMode.TITLE,
                context.getString(R.string.text_search_option_title),
                true
            ),
            TextFilter(
                MECCGTextFilterMode.TEXT.toString(),
                MECCGTextFilterMode.TEXT,
                context.getString(R.string.text_search_option_text),
                false
            ),
        )
    }

    override fun getDropdownFilterLiveData(savedStateHandle: SavedStateHandle): List<MutableLiveData<SpinnerFilter>> {
        val dropdownFilters = mutableListOf(
            alignmentLiveData(savedStateHandle),
            typeLiveData(savedStateHandle),
            setLiveData(savedStateHandle),
            keyLiveData(savedStateHandle),
        )

        /* only add dreamcard dropdown if dreamcards are enabled */
        if (shouldUseDreamcards) {
            dropdownFilters.add(
                dreamcardLiveData(savedStateHandle)
            )
            dropdownFilters.add(
                releasedLiveData(savedStateHandle)
            )
        }

        return dropdownFilters
    }

    private fun alignmentLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(
            MECCGAlignmentOption(context.getString(R.string.meccg_any_alignment))
        )
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

                val options =
                    alignments.map { MECCGAlignmentOption(it) }.sortedBy { it.displayName }
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
        val initialList = listOf(MECCGTypeOption(context.getString(R.string.meccg_any_type)))
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
        val initialList = listOf(MECCGSetOption(context.getString(R.string.meccg_any_set), "0"))
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
                    MECCGSetOption(
                        it.name ?: context.resources.getString(R.string.unknown), it.code ?: ""
                    )
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
        val initialList = listOf(MECCGKeyOption(context.getString(R.string.meccg_any_key)))
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

    private fun dreamcardLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(
            MECCGDreamcardOption(
                context.getString(R.string.meccg_all_cards),
                MECCGDreamcardOptionMode.ALL_CARDS
            ),
        )

        val defaultValue = MECCGDreamcardFilter(
            initialList,
            initialList[0]
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "dreamcardKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            val persistedData = persistedLiveData.value ?: defaultValue
            val newOptions = initialList.toMutableList()

            val options = listOf(
                MECCGDreamcardOption(
                    context.getString(R.string.meccg_no_dreamcards),
                    MECCGDreamcardOptionMode.NO_DREAMCARDS
                ),
                MECCGDreamcardOption(
                    context.getString(R.string.meccg_only_dreamcards),
                    MECCGDreamcardOptionMode.ONLY_DREAMCARDS
                ),
            )
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

        mediatorLiveData.addSource(persistedLiveData, onChangedListener)

        return mediatorLiveData
    }

    private fun releasedLiveData(savedStateHandle: SavedStateHandle): MutableLiveData<SpinnerFilter> {
        val initialList = listOf(
            MECCGReleasedOption("All Cards", MECCGReleasedOptionMode.ALL_CARDS),
            MECCGReleasedOption("Only Released", MECCGReleasedOptionMode.ONLY_RELEASED),
            MECCGReleasedOption("Only Unreleased", MECCGReleasedOptionMode.ONLY_UNRELEASED),
        )

        val defaultValue = MECCGReleasedFilter(
            initialList,
            initialList[0],
            initialList[1],
        )

        val persistedLiveData = savedStateHandle.getLiveData(
            "releasedKey",
            defaultValue
        )

        val mediatorLiveData = MediatorLiveData<SpinnerFilter>()
        mediatorLiveData.value = persistedLiveData.value

        val onChangedListener = Observer<Any> {
            val persistedData = persistedLiveData.value ?: defaultValue
            val newOptions = initialList

            if (newOptions != persistedData.options) {
                persistedData.options = newOptions

                if (!newOptions.contains(persistedData.selectedOption)) {
                    persistedData.selectedOption = newOptions[1]
                }

                persistedLiveData.value = persistedData
                mediatorLiveData.value = persistedData
            }
        }

        mediatorLiveData.addSource(persistedLiveData, onChangedListener)

        return mediatorLiveData
    }

    override fun hasAdvancedFilters(): Boolean {
        return true
    }

    override fun getNewAdvancedFilter(): AdvancedFilter {
        return MECCGAdvancedFilter(
            MECCGField.entries.map { MECCGAdvancedFilterField(it) }.sortedBy { it.displayName },
            AdvancedFilterMode.entries.toList()
        )
    }
}