package com.hatfat.cards.data

import androidx.lifecycle.LiveData

interface DataReady {
    val isDataReady: LiveData<Boolean>
}