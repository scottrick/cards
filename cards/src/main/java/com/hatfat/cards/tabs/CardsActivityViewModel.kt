package com.hatfat.cards.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Integer.max
import javax.inject.Inject

@HiltViewModel
class CardsActivityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), CardsFragmentAdapter.OnRemoveClickedListener {

    private val maxTabCount = 4
    private val nextTabNumLiveData = savedStateHandle.getLiveData<Long>("nextTabNum", 1)

    private val tabsLiveData = savedStateHandle.getLiveData<List<CardsFragmentTab>>("tabs", emptyList())
    val tabs: LiveData<List<CardsFragmentTab>>
        get() = tabsLiveData

    init {
        tabs.value?.takeIf { it.isEmpty() }?.let {
            val newList = it.toMutableList()
            newList.add(createNewTab())
            newList.add(createNewTab())
            tabsLiveData.value = newList
        }
    }

    private fun createNewTab(): CardsFragmentTab {
        val nextTabNum = nextTabNumLiveData.value ?: 1L
        nextTabNumLiveData.value = nextTabNum + 1L

        return CardsFragmentTab(nextTabNum)
    }

    fun onPageSelected(tab: TabLayout.Tab, adapter: CardsFragmentAdapter) {
        tabs.value?.let {
            if (!it[tab.position].hasTabBeenOpened) {
                it[tab.position].hasTabBeenOpened = true
                adapter.updateTabViews(tab, tab.position)
            }

            addNewTabIfNecessary()
        }
    }

    private fun addNewTabIfNecessary() {
        tabs.value?.let {
            if (it.none { tab -> !tab.hasTabBeenOpened }) {
                /* all tabs have been opened, so we need to add a new one if we aren't already at the max number of tabs */
                if (it.size < maxTabCount) {
                    val newList = it.toMutableList()
                    newList.add(createNewTab())
                    tabsLiveData.value = newList
                }
            }
        }
    }

    override fun removeTab(tabLayout: TabLayout, position: Int, adapter: CardsFragmentAdapter) {
        /* remove tab at given position */
        tabs.value?.let {
            if (position < it.size) {
                var deletingCurrentTab = false
                if (tabLayout.selectedTabPosition == position) {
                    deletingCurrentTab = true
                }

                val newList = it.toMutableList()
                newList.removeAt(position)

                if (newList.none { tab -> !tab.hasTabBeenOpened }) {
                    /* all tabs have been opened, so we need to add a new one if we aren't already at the max number of tabs */
                    if (newList.size < maxTabCount) {
                        newList.add(createNewTab())
                    }
                }

                tabsLiveData.value = newList

                if (deletingCurrentTab) {
                    var tabToSelect = tabLayout.selectedTabPosition
                    var firstNotOpened = newList.indexOfFirst { tab -> !tab.hasTabBeenOpened }
                    if (firstNotOpened == -1) {
                        firstNotOpened = it.size
                    }

                    if (tabLayout.selectedTabPosition == firstNotOpened) {
                        tabToSelect--
                        tabToSelect = max(tabToSelect, 0)
                    }

                    tabLayout.getTabAt(tabToSelect)?.let { tab ->
                        tabLayout.selectTab(tab)
                        onPageSelected(tab, adapter)
                    }
                }
            }
        }
    }
}