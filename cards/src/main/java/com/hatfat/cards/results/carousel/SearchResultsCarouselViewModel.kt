package com.hatfat.cards.results.carousel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.results.general.SearchResultsSingleCardState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchResultsCarouselViewModel @Inject constructor(
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
        get() = mediatedSearchResults.distinctUntilChanged()

    private val isFlippedLiveData = savedStateHandle.getLiveData("isFlipped", false)
    private val isRotatedLiveData = savedStateHandle.getLiveData("isRotated", false)
    private val lastSelectedPositionLiveData =
        savedStateHandle.getLiveData<Int?>("lastSelectedPosition", null)

    val isFlipped: LiveData<Boolean>
        get() = isFlippedLiveData

    val isRotated: LiveData<Boolean>
        get() = isRotatedLiveData

    val lastSelectedPosition: LiveData<Int?>
        get() = lastSelectedPositionLiveData

    private val navigateToInfoLiveData = MutableLiveData<SearchResultsSingleCardState?>()
    val navigateToInfo: LiveData<SearchResultsSingleCardState?>
        get() = navigateToInfoLiveData

    private val navigateToFullscreenLiveData = MutableLiveData<SearchResultsSingleCardState?>()
    val navigateToFullscreen: LiveData<SearchResultsSingleCardState?>
        get() = navigateToFullscreenLiveData

    fun finishedWithNavigate() {
        navigateToInfoLiveData.value = null
        navigateToFullscreenLiveData.value = null
    }

    fun infoPressed(position: Int, isFlipped: Boolean, isRotated: Boolean) {
        searchResultsLiveData.value?.let {
            val cardData = SearchResultsSingleCardState(
                position,
                it,
                isFlipped,
                isRotated
            )

            navigateToInfoLiveData.value = cardData
        }
    }

    fun bigCardPressed(position: Int, isFlipped: Boolean, isRotated: Boolean) {
        searchResultsLiveData.value?.let {
            val cardData = SearchResultsSingleCardState(
                position,
                it,
                isFlipped,
                isRotated
            )

            navigateToFullscreenLiveData.value = cardData
        }
    }

    fun rotate() {
        isRotatedLiveData.value = !(isRotatedLiveData.value ?: false)
    }

    fun flip() {
        isFlippedLiveData.value = !(isFlippedLiveData.value ?: false)
    }

    fun updateLastSelectedPosition(position: Int?) {
        lastSelectedPositionLiveData.value = position
    }

    fun setSearchResults(newResults: SearchResults) {
        searchResultsLiveData.value = newResults
    }
}