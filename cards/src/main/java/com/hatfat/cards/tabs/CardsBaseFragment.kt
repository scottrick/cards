package com.hatfat.cards.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment

open class CardsBaseFragment : Fragment() {
    var uniqueTabId: Long = 0L
    private val uniqueTabIdKey = "TAB_ID_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            uniqueTabId = it.getLong(uniqueTabIdKey)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(uniqueTabIdKey, uniqueTabId)
    }
}
