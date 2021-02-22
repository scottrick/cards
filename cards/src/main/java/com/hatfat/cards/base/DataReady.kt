package com.hatfat.cards.base

import androidx.lifecycle.LiveData

interface DataReady {
    val isDataReady: LiveData<Boolean>
}