package com.hatfat.cards.search.filter.advanced

import androidx.recyclerview.widget.DiffUtil

class AdvancedFilterDiffUtilCallback(
    private val oldList: List<AdvancedFilter>,
    private val newList: List<AdvancedFilter>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}