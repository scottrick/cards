package com.hatfat.swccg.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.hatfat.cards.data.DataReady
import com.hatfat.swccg.repo.SWCCGMetaDataRepository
import com.hatfat.swccg.repo.SWCCGCardRepository
import com.hatfat.swccg.repo.SWCCGFormatRepository
import com.hatfat.swccg.repo.SWCCGSetRepository
import javax.inject.Inject

class SWCCGDataReady @Inject constructor(
    cardRepository: SWCCGCardRepository,
    formatRepository: SWCCGFormatRepository,
    setRepository: SWCCGSetRepository,
    metaDataRepository: SWCCGMetaDataRepository
) : DataReady {

    private val mediatorLiveData: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        val onChangedListener = Observer<Boolean> {
            mediatorLiveData.value =
                (cardRepository.loaded.value == true)
                        && (formatRepository.loaded.value == true)
                        && (setRepository.loaded.value == true)
                        && (metaDataRepository.loaded.value == true)
        }

        mediatorLiveData.addSource(cardRepository.loaded, onChangedListener)
        mediatorLiveData.addSource(formatRepository.loaded, onChangedListener)
        mediatorLiveData.addSource(setRepository.loaded, onChangedListener)
        mediatorLiveData.addSource(metaDataRepository.loaded, onChangedListener)
    }

    override val isDataReady: LiveData<Boolean>
        get() = mediatorLiveData
}