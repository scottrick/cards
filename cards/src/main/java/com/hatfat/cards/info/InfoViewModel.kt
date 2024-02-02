package com.hatfat.cards.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.hatfat.cards.data.DataReady
import com.hatfat.cards.results.general.SearchResultsSingleCardState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    dataReady: DataReady
) : ViewModel() {
    private val singleCardStateLiveData = MutableLiveData<SearchResultsSingleCardState>()
    private val mediatedLiveData = MediatorLiveData<SearchResultsSingleCardState>().apply {
        val observer = Observer<Any> {
            if (dataReady.isDataReady.value == true && singleCardStateLiveData.value != null) {
                this.value = singleCardStateLiveData.value
            }
        }

        this.addSource(dataReady.isDataReady, observer)
        this.addSource(singleCardStateLiveData, observer)
    }

    val infoCardData: LiveData<SearchResultsSingleCardState>
        get() = mediatedLiveData.distinctUntilChanged()

    fun setCardData(newCardData: SearchResultsSingleCardState) {
        singleCardStateLiveData.value = newCardData
    }
}
