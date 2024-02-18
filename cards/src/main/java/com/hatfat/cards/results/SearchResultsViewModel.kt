package com.hatfat.cards.results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.general.SearchResultsSingleCardState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val dataReady: DataReady,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Base SavedStateHandle LiveData.
    private val currentPositionLiveData: MutableLiveData<Int> =
        savedStateHandle.getLiveData("pos", 0)
    private val searchResultsKeyLiveData: MutableLiveData<UUID> =
        savedStateHandle.getLiveData("resultsKey")
    private val isFlippedLiveData = savedStateHandle.getLiveData("isFlipped", false)
    private val isRotatedLiveData = savedStateHandle.getLiveData("isRotated", false)

    // Methods used to change the state.
    fun setCurrentPosition(newPosition: Int) {
        currentPositionLiveData.value = newPosition
    }

    fun setSearchResultsKey(resultsKey: UUID) {
        searchResultsKeyLiveData.value = resultsKey
    }

    fun rotate() {
        isRotatedLiveData.value = !(isRotatedLiveData.value ?: false)
    }

    fun flip() {
        isFlippedLiveData.value = !(isFlippedLiveData.value ?: false)
    }

    // Accessible LiveData.
    val searchResultsKey = MediatorLiveData<UUID>().apply {
        val observer = Observer<Any> {
            if (dataReady.isDataReady.value == true && searchResultsKeyLiveData.value != null) {
                this.value = searchResultsKeyLiveData.value
            }
        }

        this.addSource(dataReady.isDataReady, observer)
        this.addSource(searchResultsKeyLiveData, observer)
    }.distinctUntilChanged()
    val currentPosition: LiveData<Int> = currentPositionLiveData
    val isFlipped: LiveData<Boolean>
        get() = isFlippedLiveData

    val isRotated: LiveData<Boolean>
        get() = isRotatedLiveData

    fun getCurrentPosition(): Int {
        return currentPosition.value ?: 0
    }

    // Navigation LiveData.
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
        searchResultsKeyLiveData.value?.let { uuid ->
            val cardData = SearchResultsSingleCardState(
                position,
                uuid,
                isFlipped,
                isRotated
            )

            navigateToInfoLiveData.value = cardData
        }
    }

    fun bigCardPressed(position: Int, isFlipped: Boolean, isRotated: Boolean) {
        searchResultsKeyLiveData.value?.let { uuid ->
            val cardData = SearchResultsSingleCardState(
                position,
                uuid,
                isFlipped,
                isRotated
            )

            navigateToFullscreenLiveData.value = cardData
        }
    }
}
