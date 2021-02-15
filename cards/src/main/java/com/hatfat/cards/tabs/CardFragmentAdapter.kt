package com.hatfat.cards.tabs

import android.annotation.SuppressLint
import android.content.res.TypedArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter.FragmentTransactionCallback.OnPostEventListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hatfat.cards.R
import com.hatfat.cards.fragment.CardsNavigationFragment
import java.lang.Integer.max

class CardFragmentAdapter(
    fragmentActivity: FragmentActivity,
    private val tabLayout: TabLayout
) : FragmentStateAdapter(fragmentActivity), TabLayoutMediator.TabConfigurationStrategy, TabLayout.OnTabSelectedListener {

    private var nextTabNum = 1L
    private val maxTabCount = 4
    private val tabs = mutableListOf(createNewTab(), createNewTab())

    private val selectedColor: Int
    private val unselectedColor: Int

    init {
        // Add a FragmentTransactionCallback to handle changing
        // the primary navigation fragment
        registerFragmentTransactionCallback(object : FragmentTransactionCallback() {
            override fun onFragmentMaxLifecyclePreUpdated(
                fragment: Fragment,
                maxLifecycleState: Lifecycle.State
            ) = if (maxLifecycleState == Lifecycle.State.RESUMED) {
                // This fragment is becoming the active Fragment - set it to
                // the primary navigation fragment in the OnPostEventListener
                OnPostEventListener {
                    fragment.parentFragmentManager.commitNow {
                        setPrimaryNavigationFragment(fragment)
                    }
                }
            } else {
                super.onFragmentMaxLifecyclePreUpdated(fragment, maxLifecycleState)
            }
        })

        /* Get our selected and unselected highlight colors from the theme */
        val typedValue = TypedValue()
        val selected: TypedArray = fragmentActivity.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimary))
        val unselected: TypedArray = fragmentActivity.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorOnSurface))
        selectedColor = selected.getColor(0, 0)
        unselectedColor = unselected.getColor(0, 0)

        selected.recycle()
        unselected.recycle()
    }

    private fun createNewTab(): CardFragmentTab {
        return CardFragmentTab(nextTabNum++)
    }

    private fun removeTab(position: Int) {
        /* remove tab at given position */
        if (position < tabs.size) {
            var deletingCurrentTab = false
            if (tabLayout.selectedTabPosition == position) {
                deletingCurrentTab = true
            }

            val newList = tabs.toMutableList()
            newList.removeAt(position)
            updateTabsWithNewList(newList)

            addNewTabIfNecessary()

            if (deletingCurrentTab) {
                var tabToSelect = tabLayout.selectedTabPosition
                if (tabLayout.selectedTabPosition == tabs.size - 1) {
                    tabToSelect--
                    tabToSelect = max(tabToSelect, 0)
                }

                tabLayout.getTabAt(tabToSelect)?.let {
                    tabLayout.selectTab(it)
                    onPageSelected(it)
                }
            }
        }
    }

    private fun addNewTabIfNecessary() {
        if (tabs.none { !it.hasTabBeenOpened }) {
            /* all tabs have been opened, so we need to add a new one if we aren't already at the max number of tabs */
            if (tabs.size < maxTabCount) {
                val newList = tabs.toMutableList()
                newList.add(createNewTab())
                updateTabsWithNewList(newList)
            }
        }

        updateTabSelectedColors()
    }

    private fun updateTabSelectedColors() {
        for (i in 0..tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i) ?: continue
            tab.customView?.let {
                val textView = it.findViewById<TextView>(R.id.custom_tab_textview)
                textView.setTextColor(if (tab.isSelected) selectedColor else unselectedColor)
            }
        }
    }

    private fun updateTabsWithNewList(newList: List<CardFragmentTab>) {
        val diffCallback = CardFragmentDiffUtilCallback(tabs, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        tabs.clear()
        tabs.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun containsItem(itemId: Long): Boolean {
        tabs.firstOrNull { itemId == it.tabId }?.let {
            return true
        }

        return false
    }

    override fun getItemId(position: Int): Long {
        return tabs[position].tabId
    }

    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        val cardsNavigationFragment = CardsNavigationFragment()
        cardsNavigationFragment.uniqueTabId = tabs[position].tabId

        return cardsNavigationFragment
    }

    @SuppressLint("InflateParams")
    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        val customView = LayoutInflater.from(tab.view.context).inflate(R.layout.view_custom_tab, null)
        tab.customView = customView

        updateTabViews(tab, position)
    }

    private fun updateTabViews(tab: TabLayout.Tab, position: Int) {
        tab.customView?.let {
            val textView = it.findViewById<TextView>(R.id.custom_tab_textview)
            val buttonImageView = it.findViewById<ImageView>(R.id.button_imageview)

            val fragmentTab = tabs[position]
            if (fragmentTab.hasTabBeenOpened) {
                buttonImageView.isClickable = true
                buttonImageView.setOnClickListener {
                    removeTab(position)
                }

                buttonImageView.setImageResource(R.drawable.tab_close)
                textView.text = it.context.getString(R.string.tabLabel, fragmentTab.tabId.toString())
            } else {
                buttonImageView.setOnClickListener(null)
                buttonImageView.isClickable = false

                buttonImageView.setImageResource(R.drawable.tab_new_plus)
                textView.text = ""
            }
        }
    }

    fun onPageSelected(tab: TabLayout.Tab) {
        if (!tabs[tab.position].hasTabBeenOpened) {
            tabs[tab.position].hasTabBeenOpened = true
            updateTabViews(tab, tab.position)
        }

        addNewTabIfNecessary()
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            updateTabSelectedColors()
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }
}