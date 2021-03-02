package com.hatfat.cards.search

import androidx.lifecycle.*
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.SearchResults
import com.hatfat.cards.search.param.BasicTextSearchParam
import com.hatfat.cards.search.param.SearchParams
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

    private val textSearchOptionsLiveDataList = mutableListOf<MutableLiveData<BasicTextSearchParam>>().apply {
        cardSearchOptionsProvider.getTextSearchOptions().forEach { textSearchOption ->
            val textSearchOptionLiveData = savedStateHandle.getLiveData(textSearchOption.searchOptionKey.toString(), textSearchOption)
            this.add(textSearchOptionLiveData)
        }
    }
    val basicTextSearchOptions: List<LiveData<BasicTextSearchParam>>
        get() = textSearchOptionsLiveDataList

    private val searchTextEnabledLiveData = MediatorLiveData<Boolean>().apply {
        val observer = Observer<BasicTextSearchParam> { options ->
            this.value = textSearchOptionsLiveDataList.any {
                it.value?.isEnabled ?: false
            }
        }

        textSearchOptionsLiveDataList.forEach {
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
        textSearchOptionsLiveDataList.forEach {
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
        val basicTextSearchFilters = textSearchOptionsLiveDataList.map {
            it.value
        }.filterNotNull().filter { it.isEnabled }

        val searchConfig = SearchParams(searchString.value ?: "", basicTextSearchFilters)
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

    fun textSearchOptionCheckedChanged(filterBasic: BasicTextSearchParam, isChecked: Boolean) {
        filterBasic.isEnabled = isChecked

        textSearchOptionsLiveDataList.find {
            it.value?.searchOptionKey == filterBasic.searchOptionKey
        }?.also {
            it.value = filterBasic
        }
    }
}