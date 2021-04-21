package com.hatfat.trek1.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.hatfat.cards.data.DataReady
import com.hatfat.trek1.repo.Trek1CardRepository
import com.hatfat.trek1.repo.Trek1MetaDataRepository
import com.hatfat.trek1.repo.Trek1SetRepository
import javax.inject.Inject

class Trek1DataReady @Inject constructor(
    cardRepository: Trek1CardRepository,
    setRepository: Trek1SetRepository,
    metaDataRepository: Trek1MetaDataRepository
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