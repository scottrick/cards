package com.hatfat.cards.search

import android.util.Log
import androidx.lifecycle.*
import com.hatfat.cards.base.DataReady
import com.hatfat.cards.results.SearchResults
import com.hatfat.cards.temp.FakeSearchResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class CardSearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataReady: DataReady,
    cardSearchOptionsProvider: CardSearchOptionsProvider
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

    private val textSearchOptionsLiveDataList = mutableListOf<MutableLiveData<TextSearchOption>>().apply {
        cardSearchOptionsProvider.getTextSearchOptions().forEach { textSearchOption ->
            val textSearchOptionLiveData = savedStateHandle.getLiveData(textSearchOption.seachOptionKey.toString(), textSearchOption)
            this.add(textSearchOptionLiveData)
        }
    }
    val textSearchOptions: List<LiveData<TextSearchOption>>
        get() = textSearchOptionsLiveDataList

    private val searchTextEnabledLiveData = MediatorLiveData<Boolean>().apply {
        val observer = Observer<TextSearchOption> { options ->
            this.value = textSearchOptionsLiveDataList.any {
                it.value?.isChecked ?: false
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

    }

    fun searchPressed() {
        stateLiveData.value = State.SEARCHING
        GlobalScope.launch(Dispatchers.IO) {
            Log.e("catfat", "search text ${searchString.value}")
            textSearchOptionsLiveDataList.forEach {
                it.value?.let {
                    Log.e("catfat", "searching with ${it.seachOptionKey} ${it.isChecked}")
                }
            }

            doSearch()
        }
    }

    private suspend fun doSearch() {
        delay(550) //catfat

        withContext(Dispatchers.Main) {
            searchResultsLiveData.value = FakeSearchResults(listOf(1, 2, 3))
            stateLiveData.value = State.ENTERING_INFO
        }
    }

    fun finishedWithSearchResults() {
        searchResultsLiveData.value = null
    }

    fun setSearchString(newValue: String) {
        searchStringLiveData.value = newValue
    }

    fun textSearchOptionCheckedChanged(option: TextSearchOption, isChecked: Boolean) {
        option.isChecked = isChecked

        textSearchOptionsLiveDataList.find {
            it.value?.seachOptionKey == option.seachOptionKey
        }?.also {
            it.value = option
        }
    }
}