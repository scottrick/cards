package com.hatfat.cards.search

import androidx.lifecycle.*
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.search.filter.SearchParams
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.TextFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilterAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataReady: DataReady,
    private val cardSearchOptionsProvider: CardSearchOptionsProvider,
    private val cardSearchHandler: CardSearchHandler
) : ViewModel(), AdvancedFilterAdapter.AdvancedFilterHandlerInterface {

    enum class State {
        LOADING,
        ENTERING_INFO,
        SEARCHING,
    }

    private val stateLiveData = MediatorLiveData<State>().apply {
        /* set initial value to loading */
        this.value = State.LOADING
        this.addSource(dataReady.isDataReady) {
            if (!it) {
                this.value = State.LOADING
            } else if (it && this.value == State.LOADING) {
                this.value = State.ENTERING_INFO
            }
        }
    }
    val state: LiveData<State>
        get() = stateLiveData.distinctUntilChanged()

    private val searchStringLiveData = savedStateHandle.getLiveData<String>("searchStringKey")
    val searchString: LiveData<String>
        get() = searchStringLiveData

    private val textSearchFiltersLiveDataList = mutableListOf<MutableLiveData<TextFilter>>().apply {
        cardSearchOptionsProvider.getTextSearchOptions().forEach { textSearchOption ->
            val textSearchOptionLiveData =
                savedStateHandle.getLiveData(textSearchOption.liveDataKey, textSearchOption)
            this.add(textSearchOptionLiveData)
        }
    }
    val textSearchFilters: List<LiveData<TextFilter>>
        get() = textSearchFiltersLiveDataList

    private val spinnerFiltersLiveData =
        cardSearchOptionsProvider.getDropdownFilterLiveData(savedStateHandle)
    val spinnerFilters: List<LiveData<SpinnerFilter>>
        get() = spinnerFiltersLiveData

    private val advancedFiltersLiveData =
        savedStateHandle.getLiveData<List<AdvancedFilter>>("advancedFilters", emptyList())
    val advancedFilters: LiveData<List<AdvancedFilter>>
        get() = advancedFiltersLiveData

    private val searchTextEnabledLiveData = MediatorLiveData<Boolean>().apply {
        val observer = Observer<TextFilter> {
            this.value = textSearchFiltersLiveDataList.any {
                it.value?.isEnabled ?: false
            }
        }

        textSearchFiltersLiveDataList.forEach {
            this.addSource(it, observer)
        }
    }
    val searchTextEnabled: LiveData<Boolean>
        get() = searchTextEnabledLiveData

    private val searchResultsLiveData = MutableLiveData<SearchResults?>()
    val searchResults: LiveData<SearchResults?>
        get() = searchResultsLiveData

    val hasAdvancedFilters: Boolean
        get() = cardSearchOptionsProvider.hasAdvancedFilters()

    private val isAddCustomFilterEnabledLiveData = MediatorLiveData<Boolean>().apply {
        this.addSource(advancedFiltersLiveData) {
            this.value = it.size < 4
        }
    }
    val isAddCustomFilterEnabled: LiveData<Boolean>
        get() = isAddCustomFilterEnabledLiveData

    fun newAdvancedFilterPressed() {
        val newList = advancedFiltersLiveData.value?.toMutableList() ?: mutableListOf()
        cardSearchOptionsProvider.getNewAdvancedFilter()?.let {
            newList.add(0, it)
            advancedFiltersLiveData.value = newList
        }
    }

    private fun removeAdvancedFilter(position: Int) {
        val options = advancedFiltersLiveData.value?.toMutableList() ?: mutableListOf()

        if (position >= 0 && position < options.size) {
            options.removeAt(position)
            advancedFiltersLiveData.value = options
        }
    }

    fun resetPressed() {
        searchStringLiveData.value = ""
        textSearchFiltersLiveDataList.forEach {
            it.value?.let { filterBasic ->
                filterBasic.isEnabled = filterBasic.isEnabledByDefault
                it.value = filterBasic
            }
        }

        spinnerFiltersLiveData.forEach {
            it.value?.let { spinnerFilter ->
                spinnerFilter.selectedOption = spinnerFilter.defaultOption
                it.value = spinnerFilter
            }
        }

        advancedFiltersLiveData.value = mutableListOf()
    }

    fun searchPressed() {
        stateLiveData.value = State.SEARCHING
        viewModelScope.launch(Dispatchers.IO) {
            doSearch()
        }
    }

    private suspend fun doSearch() {
        val textFilters = textSearchFiltersLiveDataList.mapNotNull {
            it.value
        }.filter { it.isEnabled }

        val spinners = spinnerFilters.mapNotNull {
            it.value
        }.filter {
            it.selectedOption != it.notSelectedOption
        }

        val advFilters = advancedFilters.value?.filter {
            it.isValid
        } ?: emptyList()

        /* ignore surrounding whitespace */
        val searchStringToUse = searchString.value?.trim() ?: ""

        val searchConfig = SearchParams(searchStringToUse, textFilters, spinners, advFilters)
        val searchResults = cardSearchHandler.performSearch(searchConfig)

        withContext(Dispatchers.Main) {
            searchResultsLiveData.value = searchResults
            stateLiveData.value = State.ENTERING_INFO
        }
    }

    fun finishedWithSearchResults() {
        searchResultsLiveData.value = null
    }

    fun setSearchString(newValue: String) {
        searchStringLiveData.value = newValue
    }

    fun textSearchOptionCheckedChanged(filter: TextFilter, isChecked: Boolean) {
        filter.isEnabled = isChecked

        textSearchFiltersLiveDataList.find {
            it.value?.liveDataKey == filter.liveDataKey
        }?.also {
            it.value = filter
        }
    }

    fun dropDownFilterSelectionChanged(dropDownFilter: SpinnerFilter) {
        spinnerFiltersLiveData.find {
            it.value == dropDownFilter
        }?.also {
            it.value = dropDownFilter
        }
    }

    override fun onDeletePressed(position: Int) {
        removeAdvancedFilter(position)
    }

    override fun positionFilterWasUpdated(position: Int) {
        advancedFiltersLiveData.value =
            advancedFiltersLiveData.value?.toMutableList() ?: mutableListOf()
    }
}