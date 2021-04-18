package com.hatfat.trek2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.hatfat.cards.data.DataReady
import javax.inject.Inject

class Trek2DataReady @Inject constructor(
//    cardRepository: MECCGCardRepository,
//    setRepository: MECCGSetRepository,
//    metaDataRepository: MECCGMetaDataRepository
) : DataReady {

    private val mediatorLiveData: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
//        val onChangedListener = Observer<Boolean> {
//            mediatorLiveData.value =
//                (cardRepository.loaded.value == true)
//                        && (setRepository.loaded.value == true)
//                        && (metaDataRepository.loaded.value == true)
//        }

//        mediatorLiveData.addSource(cardRepository.loaded, onChangedListener)
//        mediatorLiveData.addSource(setRepository.loaded, onChangedListener)
//        mediatorLiveData.addSource(metaDataRepository.loaded, onChangedListener)
        mediatorLiveData.value = true
    }

    override val isDataReady: LiveData<Boolean>
        get() = mediatorLiveData
}