package com.hatfat.cards.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class CardRepository {
    protected val loadedLiveData = MutableLiveData<Boolean>()

    val loaded: LiveData<Boolean>
        get() = loadedLiveData

    init {
        loadedLiveData.value = false
    }
}