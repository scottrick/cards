package com.hatfat.cards.results.list

import androidx.lifecycle.*
import com.hatfat.cards.data.CurrentPosition
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.data.SearchResults
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchResultsListViewModel @Inject constructor(
    dataReady: DataReady
) : ViewModel(), SearchResultsListAdapter.OnCardSelectedInterface {

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

    private val currentPositionLiveData = MutableLiveData(0)
    val currentPosition: LiveData<Int>
        get() = currentPositionLiveData

    private val navigateToLiveData = MutableLiveData<SearchResults?>()
    val navigateTo: LiveData<SearchResults?>
        get() = navigateToLiveData

    fun setSearchResults(newResults: SearchResults) {
        searchResultsLiveData.value = newResults
    }

    fun finishedWithNavigateTo() {
        navigateToLiveData.value = null
    }

    override fun onCardPressed(position: Int) {
        searchResultsLiveData.value?.let {
            it.currentPosition = position
            navigateToLiveData.value = it
        }
    }
}