package com.hatfat.cards.fullscreen

import androidx.lifecycle.*
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.data.card.SingleCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FullscreenCardViewModel @Inject constructor(
    dataReady: DataReady,
) : ViewModel() {
    private val fullscreenCardLiveData = MutableLiveData<SingleCardData>()
    private val mediatedLiveData = MediatorLiveData<SingleCardData>().apply {
        val observer = Observer<Any> {
            if (dataReady.isDataReady.value == true && fullscreenCardLiveData.value != null) {
                this.value = fullscreenCardLiveData.value
            }
        }

        this.addSource(dataReady.isDataReady, observer)
        this.addSource(fullscreenCardLiveData, observer)
    }

    val fullscreenCard: LiveData<SingleCardData>
        get() = Transformations.distinctUntilChanged(mediatedLiveData)

    fun setCardData(newCardData: SingleCardData) {
        fullscreenCardLiveData.value = newCardData
    }
}
