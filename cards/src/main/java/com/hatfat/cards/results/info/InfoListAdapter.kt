package com.hatfat.cards.results.info

import android.annotation.SuppressLint
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

class InfoListAdapter : RecyclerView.Adapter<InfoListViewHolder>() {

    var infoList: List<String>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InfoListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_info_list_item, parent, false)

        return InfoListViewHolder(view)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: InfoListViewHolder, position: Int) {
        val itemString = infoList?.get(position) ?: ""
        val itemHtmlString: Spanned = Html.fromHtml(itemString)
        holder.textView.text = itemHtmlString
    }

    override fun getItemCount(): Int {
        return infoList?.size ?: 0
    }
}