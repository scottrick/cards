package com.hatfat.cards.search.filter.advanced

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

class AdvancedFilterViewHolder(
    itemView: View,
    advancedFilterHandler: AdvancedFilterAdapter.AdvancedFilterHandlerInterface?
) : RecyclerView.ViewHolder(itemView) {
    val filterTextEditText: EditText = itemView.findViewById(R.id.advanced_filter_edittext)
    private val deleteButton: ImageView =
        itemView.findViewById(R.id.advanced_filter_delete_imageview)
    val fieldSpinner: Spinner = itemView.findViewById(R.id.advanced_filter_field_spinner)
    val modeSpinner: Spinner = itemView.findViewById(R.id.advanced_filter_mode_spinner)

    init {
        deleteButton.setOnClickListener {
            advancedFilterHandler?.onDeletePressed(bindingAdapterPosition)
        }
    }
}