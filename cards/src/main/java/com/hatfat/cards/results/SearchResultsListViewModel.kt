package com.hatfat.cards.results

import androidx.lifecycle.*
import com.hatfat.cards.base.DataReady
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchResultsListViewModel @Inject constructor(
    dataReady: DataReady
) : ViewModel() {

    private val searchResultsLiveData = MutableLiveData<SearchResults>()
    private val mediatedSearchResults = MediatorLiveData<SearchResults>().apply {
        val observer = Observer<Any> {
            if (dataReady.isDataReady.value == true && searchResultsLiveData.value != null) {
                this.value = searchResultsLiveData.value
            }
        }

        this.addSource(dataReady.isDataReady, observer)
        this.addSource(searchResultsLiveData, observer)
    }
    val searchResults: LiveData<SearchResults>
        get() = Transformations.distinctUntilChanged(mediatedSearchResults)

    fun setSearchResults(newResults: SearchResults) {
        searchResultsLiveData.value = newResults
    }
}