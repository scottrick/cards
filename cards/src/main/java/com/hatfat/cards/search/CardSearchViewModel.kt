package com.hatfat.cards.search

import androidx.lifecycle.*
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.SearchResults
import com.hatfat.cards.search.filter.SearchParams
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.TextFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataReady: DataReady,
    cardSearchOptionsProvider: CardSearchOptionsProvider,
    private val cardSearchHandler: CardSearchHandler
) : ViewModel() {

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
        get() = Transformations.distinctUntilChanged(stateLiveData)

    private val searchStringLiveData = savedStateHandle.getLiveData<String>("searchStringKey")
    val searchString: LiveData<String>
        get() = searchStringLiveData

    private val textSearchFiltersLiveDataList = mutableListOf<MutableLiveData<TextFilter>>().apply {
        cardSearchOptionsProvider.getTextSearchOptions().forEach { textSearchOption ->
            val textSearchOptionLiveData = savedStateHandle.getLiveData(textSearchOption.liveDataKey.toString(), textSearchOption)
            this.add(textSearchOptionLiveData)
        }
    }
    val textSearchFilters: List<LiveData<TextFilter>>
        get() = textSearchFiltersLiveDataList

    private val spinnerSearchFiltersLiveDataList = mutableListOf<MutableLiveData<SpinnerFilter>>().apply {
        cardSearchOptionsProvider.getDropdownFilterOptions().forEach { dropdownFilter ->
            val dropdownSearchOptionListData = savedStateHandle.getLiveData(dropdownFilter.liveDataKey, dropdownFilter)
            this.add(dropdownSearchOptionListData)
        }
    }
    val spinnerSearchFilters: List<LiveData<SpinnerFilter>>
        get() = spinnerSearchFiltersLiveDataList

    private val searchTextEnabledLiveData = MediatorLiveData<Boolean>().apply {
        val observer = Observer<TextFilter> { options ->
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

    fun resetPressed() {
        searchStringLiveData.value = ""
        textSearchFiltersLiveDataList.forEach {
            it.value?.let { filterBasic ->
                filterBasic.isEnabled = filterBasic.isEnabledByDefault
                it.value = filterBasic
            }
        }
    }

    fun searchPressed() {
        stateLiveData.value = State.SEARCHING
        GlobalScope.launch(Dispatchers.IO) {
            doSearch()
        }
    }

    private suspend fun doSearch() {
        val basicTextSearchFilters = textSearchFiltersLiveDataList.map {
            it.value
        }.filterNotNull().filter { it.isEnabled }

        val searchConfig = SearchParams(searchString.value ?: "", basicTextSearchFilters, emptyList())
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

    // catfat test restoring filter state!!
    fun dropDownFilterSelectionChanged(dropDownFilter: SpinnerFilter) {
        spinnerSearchFiltersLiveDataList.find {
            it.value?.liveDataKey == dropDownFilter.liveDataKey
        }?.also {
            it.value = dropDownFilter
        }
    }
}