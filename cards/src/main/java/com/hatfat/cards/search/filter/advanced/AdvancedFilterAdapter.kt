package com.hatfat.cards.search.filter.advanced

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hatfat.cards.R
import com.hatfat.cards.search.CardSearchResetViewHolder

//
// Contains the different advanced filters along with the bottom search & reset view.
//
class AdvancedFilterAdapter : RecyclerView.Adapter<ViewHolder>() {

    var handler: AdvancedFilterHandlerInterface? = null

    var filters: List<AdvancedFilter> = emptyList()
        set(value) {
            val diffCallback = AdvancedFilterDiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.view_advanced_filter_row -> {
                AdvancedFilterViewHolder(view, handler)
            }

            else -> {
                CardSearchResetViewHolder(view, handler)
            }
        }
    }

    override fun getItemCount(): Int {
        return filters.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < filters.size) {
            R.layout.view_advanced_filter_row
        } else {
            R.layout.view_search_and_reset
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (viewHolder.itemViewType == R.layout.view_advanced_filter_row) {
            val holder = viewHolder as AdvancedFilterViewHolder

            filters[position].let { advancedFilter ->
                val fieldAdapter = ArrayAdapter(
                    holder.fieldSpinner.context,
                    R.layout.view_dropdown_item,
                    advancedFilter.fields.map { it.displayName })
                fieldAdapter.setDropDownViewResource(R.layout.view_dropdown_item)
                holder.fieldSpinner.adapter = fieldAdapter
                holder.fieldSpinner.setSelection(advancedFilter.selectedFieldIndex)

                holder.fieldSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            fieldPosition: Int,
                            id: Long
                        ) {
                            if (advancedFilter.selectedFieldIndex != fieldPosition) {
                                advancedFilter.selectedFieldIndex = fieldPosition
                                handler?.positionFilterWasUpdated(holder.absoluteAdapterPosition)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            /* shouldn't happen */
                            throw RuntimeException("Nothing Selected!")
                        }
                    }

                val modeAdapter = ArrayAdapter(
                    holder.modeSpinner.context,
                    R.layout.view_dropdown_item,
                    advancedFilter.modes.map { holder.modeSpinner.resources.getString(it.displayNameResource) })
                modeAdapter.setDropDownViewResource(R.layout.view_dropdown_item)
                holder.modeSpinner.adapter = modeAdapter
                holder.modeSpinner.setSelection(advancedFilter.selectedModeIndex)

                holder.modeSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            modePosition: Int,
                            id: Long
                        ) {
                            if (advancedFilter.selectedModeIndex != modePosition) {
                                advancedFilter.selectedModeIndex = modePosition
                                handler?.positionFilterWasUpdated(holder.absoluteAdapterPosition)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            /* shouldn't happen */
                            throw RuntimeException("Nothing Selected!")
                        }
                    }

                holder.filterTextEditText.setText(advancedFilter.inputValue)
                holder.filterTextEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        advancedFilter.inputValue = s.toString()
                        handler?.positionFilterWasUpdated(holder.absoluteAdapterPosition)
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }
                })
            }
        }
    }

    interface AdvancedFilterHandlerInterface {
        fun onDeletePressed(position: Int)
        fun positionFilterWasUpdated(position: Int)
        fun onSearchPressed()
        fun onResetPressed()
    }
}