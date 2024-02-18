package com.hatfat.cards.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

abstract class CardsRepository {

    @Inject
    @Named("RepositoryScope")
    protected lateinit var coroutineScope: CoroutineScope

    @Inject
    @Named("RepositoryDispatcher")
    protected lateinit var coroutineDispatcher: CoroutineDispatcher

    protected val loadedLiveData = MutableLiveData<Boolean>()

    /* subclasses should implement this if they have work to do on the main thread before loading */
    protected open fun setup() {}

    /* subclasses should implement this if they have background loading tasks */
    protected open suspend fun load() {
        withContext(Dispatchers.Main) {
            loadedLiveData.value = true
        }
    }

    /* called in application onCreate to setup the repository */
    fun prepare() {
        setup()

        coroutineScope.launch(coroutineDispatcher) {
            load()
        }
    }

    val loaded: LiveData<Boolean>
        get() = loadedLiveData

    init {
        loadedLiveData.value = false
    }
}