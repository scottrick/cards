package com.hatfat.cards.info

import androidx.lifecycle.*
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.data.card.SingleCardData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    dataReady: DataReady
) : ViewModel() {
    private val infoCardDataLiveData = MutableLiveData<SingleCardData>()
    private val mediatedLiveData = MediatorLiveData<SingleCardData>().apply {
        val observer = Observer<Any> {
            if (dataReady.isDataReady.value == true && infoCardDataLiveData.value != null) {
                this.value = infoCardDataLiveData.value
            }
        }

        this.addSource(dataReady.isDataReady, observer)
        this.addSource(infoCardDataLiveData, observer)
    }

    val infoCardData: LiveData<SingleCardData>
        get() = mediatedLiveData.distinctUntilChanged()

    fun setCardData(newCardData: SingleCardData) {
        infoCardDataLiveData.value = newCardData
    }
}
