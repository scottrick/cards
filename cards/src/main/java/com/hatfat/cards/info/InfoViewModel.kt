package com.hatfat.cards.info

import androidx.lifecycle.*
import com.hatfat.cards.data.DataReady
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    dataReady: DataReady
) : ViewModel() {

    private val infoSelectionLiveData = MutableLiveData<InfoSelection>()
    private val mediatedLiveData = MediatorLiveData<InfoSelection>().apply {
        val observer = Observer<Any> {
            if (dataReady.isDataReady.value == true && infoSelectionLiveData.value != null) {
                this.value = infoSelectionLiveData.value
            }
        }

        this.addSource(dataReady.isDataReady, observer)
        this.addSource(infoSelectionLiveData, observer)
    }

    val infoSelection: LiveData<InfoSelection>
        get() = Transformations.distinctUntilChanged(mediatedLiveData)

    fun setInfoSelection(newData: InfoSelection) {
        infoSelectionLiveData.value = newData
    }
}
