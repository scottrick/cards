package com.hatfat.cards.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class CardsRepository {
    protected val loadedLiveData = MutableLiveData<Boolean>()

    val loaded: LiveData<Boolean>
        get() = loadedLiveData

    init {
        loadedLiveData.value = false
    }
}