package com.hatfat.cards.tabs

import androidx.recyclerview.widget.DiffUtil

class CardsFragmentDiffUtilCallback(
    private val oldList: List<CardsFragmentTab>,
    private val newList: List<CardsFragmentTab>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].tabId == newList[newItemPosition].tabId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].tabId == newList[newItemPosition].tabId
    }
}
