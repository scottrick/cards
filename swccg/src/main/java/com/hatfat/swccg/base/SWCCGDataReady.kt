package com.hatfat.swccg.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.hatfat.cards.base.DataReady
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.repo.SWCCGFormatRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import javax.inject.Inject

class SWCCGDataReady @Inject constructor(
    cardRepository: SWCCGCardRepository,
    formatRepository: SWCCGFormatRepository,
    setRepository: SWCCGSetRepository
) : DataReady {

    private val mediatorLiveData: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        val onChangedListener = Observer<Boolean> {
            mediatorLiveData.value =
                (cardRepository.loaded.value == true)
                        && (formatRepository.loaded.value == true)
                        && (setRepository.loaded.value == true)
        }

        mediatorLiveData.addSource(cardRepository.loaded, onChangedListener)
        mediatorLiveData.addSource(formatRepository.loaded, onChangedListener)
        mediatorLiveData.addSource(setRepository.loaded, onChangedListener)
    }

    override val isDataReady: LiveData<Boolean>
        get() = mediatorLiveData
}