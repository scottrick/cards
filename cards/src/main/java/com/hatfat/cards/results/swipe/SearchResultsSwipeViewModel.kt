package com.hatfat.cards.results.swipe

import androidx.lifecycle.*
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.data.SearchResults
import com.hatfat.cards.data.card.SingleCardData
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
    private val lastSelectedPositionLiveData =
        savedStateHandle.getLiveData<Int?>("lastSelectedPosition", null)

    val isFlipped: LiveData<Boolean>
        get() = isFlippedLiveData

    val isRotated: LiveData<Boolean>
        get() = isRotatedLiveData

    val lastSelectedPosition: LiveData<Int?>
        get() = lastSelectedPositionLiveData

    private val navigateToInfoLiveData = MutableLiveData<SingleCardData?>()
    val navigateToInfo: LiveData<SingleCardData?>
        get() = navigateToInfoLiveData

    private val navigateToFullscreenLiveData = MutableLiveData<SingleCardData?>()
    val navigateToFullscreen: LiveData<SingleCardData?>
        get() = navigateToFullscreenLiveData

    fun finishedWithNavigate() {
        navigateToInfoLiveData.value = null
        navigateToFullscreenLiveData.value = null
    }

    fun infoPressed(position: Int, isFlipped: Boolean, isRotated: Boolean) {
        searchResultsLiveData.value?.let {
            val cardData = SingleCardData(
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
            val cardData = SingleCardData(
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