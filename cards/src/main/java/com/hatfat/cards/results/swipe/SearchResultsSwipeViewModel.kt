package com.hatfat.cards.results.swipe

import androidx.lifecycle.*
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.SearchResults
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchResultsSwipeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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

    private val isFlippedLiveData = savedStateHandle.getLiveData("isFlipped", false)
    private val isRotatedLiveData = savedStateHandle.getLiveData("isRotated", false)

    val isFlipped: LiveData<Boolean>
        get() = isFlippedLiveData

    val isRotated: LiveData<Boolean>
        get() = isRotatedLiveData

    fun rotate() {
        isRotatedLiveData.value = !(isRotatedLiveData.value ?: false)
    }

    fun flip() {
        isFlippedLiveData.value = !(isFlippedLiveData.value ?: false)
    }

    fun setSearchResults(newResults: SearchResults) {
        searchResultsLiveData.value = newResults
    }
}