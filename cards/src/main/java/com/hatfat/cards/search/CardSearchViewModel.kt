package com.hatfat.cards.search

import android.util.Log
import androidx.lifecycle.*
import com.hatfat.cards.base.DataReady
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardSearchViewModel @Inject constructor(
    private val dataReady: DataReady,
    private val searchOptionsProvider: SearchOptionsProvider
) : ViewModel() {

    enum class State {
        LOADING,
        ENTERING_INFO,
        SEARCHING,
    }

    private val stateLiveData = MediatorLiveData<State>().apply {
        /* set initial value to loading */
        this.value = State.LOADING
        this.addSource(dataReady.isDataReady) {
            if (!it) {
                this.value = State.LOADING
            } else if (it && this.value == State.LOADING) {
                this.value = State.ENTERING_INFO
            }
        }
    }
    val state: LiveData<State>
        get() = Transformations.distinctUntilChanged(stateLiveData)

    private val searchStringLiveData = MutableLiveData<String>()
    val searchString: LiveData<String>
        get() = searchStringLiveData

    /*
    private val textSearchOptionsLiveDataList = mutableListOf<MutableLiveData<TextSearchOption>>().apply {
        searchOptionsProvider.getTextSearchOptions().forEach { textSearchOption ->
            val mutableTextSearchOption = MutableLiveData<TextSearchOption>().apply {
                this.value = textSearchOption
            }

            this.add(mutableTextSearchOption)
        }
    }
    val textSearchOptions: List<LiveData<TextSearchOption>>
        get() = textSearchOptionsLiveDataList
     */

    private val textSearchOptionsLiveData = MutableLiveData(searchOptionsProvider.getTextSearchOptions())
    val textSearchOptions: LiveData<List<TextSearchOption>>
        get() = Transformations.distinctUntilChanged(textSearchOptionsLiveData)

    private val searchTextEnabledLiveData = MediatorLiveData<Boolean>().apply {
        val observer = Observer<List<TextSearchOption>> { options ->
            updateTextSearchEnabled()
        }

        this.addSource(textSearchOptionsLiveData, observer)
    }
    val searchTextEnabled: LiveData<Boolean>
        get() = searchTextEnabledLiveData

    fun resetPressed() {

    }

    fun searchPressed() {
        stateLiveData.value = State.SEARCHING
        GlobalScope.launch(Dispatchers.IO) {
            Log.e("catfat", "search text ${searchString.value}")
            textSearchOptionsLiveData.value?.forEach {
                Log.e("catfat", "searching with ${it.seachOptionKey} ${it.isChecked}")
            }

            doSearch()
        }
    }

    private suspend fun doSearch() {

    }

    fun setSearchString(newValue: String) {
        searchStringLiveData.value = newValue
    }

    fun setTextSearchOptionCheckedValue(option: TextSearchOption, isChecked: Boolean) {
        option.isChecked = isChecked

        updateTextSearchEnabled()
    }

    private fun updateTextSearchEnabled() {
        searchTextEnabledLiveData.value = textSearchOptionsLiveData.value?.any { it.isChecked }
    }
}