package com.hatfat.swccg.temp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.hatfat.cards.temp.TestListInterface
import com.hatfat.swccg.repo.SWCCGCardsRepository
import javax.inject.Inject

class TempListInterfaceImpl @Inject constructor(
    swccgCardRepository: SWCCGCardsRepository
) : TestListInterface {
    val mediatorLiveData: MediatorLiveData<List<String>> = MediatorLiveData()

    init {
        mediatorLiveData.addSource(swccgCardRepository.sortedCardsArray) { cardArray ->
            mediatorLiveData.value = cardArray.map { it.front.sortableTitle }
        }
    }

    override val stringsLiveData: LiveData<List<String>>
        get() = mediatorLiveData
}