package com.hatfat.trek2.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.hatfat.cards.data.DataReady
import com.hatfat.trek2.repo.Trek2CardRepository
import com.hatfat.trek2.repo.Trek2MetaDataRepository
import com.hatfat.trek2.repo.Trek2SetRepository
import javax.inject.Inject

class Trek2DataReady @Inject constructor(
    cardRepository: Trek2CardRepository,
    setRepository: Trek2SetRepository,
    metaDataRepository: Trek2MetaDataRepository
) : DataReady {

    private val mediatorLiveData: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        val onChangedListener = Observer<Boolean> {
            mediatorLiveData.value =
                (cardRepository.loaded.value == true)
                        && (setRepository.loaded.value == true)
                        && (metaDataRepository.loaded.value == true)
        }

        mediatorLiveData.addSource(cardRepository.loaded, onChangedListener)
        mediatorLiveData.addSource(setRepository.loaded, onChangedListener)
        mediatorLiveData.addSource(metaDataRepository.loaded, onChangedListener)
    }

    override val isDataReady: LiveData<Boolean>
        get() = mediatorLiveData
}