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
import com.hatfat.cards.R

class AdvancedFilterAdapter : RecyclerView.Adapter<AdvancedFilterViewHolder>() {

    var handler: AdvancedFilterHandlerInterface? = null

    var filters: List<AdvancedFilter> = emptyList()
        set(value) {
            val diffCallback = AdvancedFilterDiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvancedFilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_advanced_filter_row, parent, false)
        return AdvancedFilterViewHolder(view, handler)
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun onBindViewHolder(holder: AdvancedFilterViewHolder, position: Int) {
        filters[position].let { advancedFilter ->
            val fieldAdapter = ArrayAdapter(holder.fieldSpinner.context, R.layout.view_dropdown_item, advancedFilter.fields.map { it.displayName })
            fieldAdapter.setDropDownViewResource(R.layout.view_dropdown_item)
            holder.fieldSpinner.adapter = fieldAdapter
            holder.fieldSpinner.setSelection(advancedFilter.selectedFieldIndex)

            holder.fieldSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, fieldPosition: Int, id: Long) {
                    if (advancedFilter.selectedFieldIndex != fieldPosition) {
                        advancedFilter.selectedFieldIndex = fieldPosition
                        handler?.positionFilterWasUpdated(position)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    /* shouldn't happen */
                    throw RuntimeException("Nothing Selected!")
                }
            }

            val modeAdapter = ArrayAdapter(holder.modeSpinner.context, R.layout.view_dropdown_item, advancedFilter.modes.map { it.displayName })
            modeAdapter.setDropDownViewResource(R.layout.view_dropdown_item)
            holder.modeSpinner.adapter = modeAdapter
            holder.modeSpinner.setSelection(advancedFilter.selectedModeIndex)

            holder.modeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, modePosition: Int, id: Long) {
                    if (advancedFilter.selectedModeIndex != modePosition) {
                        advancedFilter.selectedModeIndex = modePosition
                        handler?.positionFilterWasUpdated(position)
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
                    handler?.positionFilterWasUpdated(position)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    interface AdvancedFilterHandlerInterface {
        fun onDeletePressed(position: Int)

        fun positionFilterWasUpdated(position: Int)
    }
}