package com.hatfat.cards.temp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R

class TestStringAdapter : RecyclerView.Adapter<TestStringAdapter.StringViewHolder>() {

    private var stringList: List<String> = emptyList()

    fun updateStringList(newList: List<String>) {
        stringList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.string_test, parent, false)
        return StringViewHolder(view)
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.bind(stringList[position])
    }

    override fun getItemCount(): Int {
        return stringList.size
    }

    class StringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView as TextView
        fun bind(text: String) {
            textView.text = text
        }
    }
}