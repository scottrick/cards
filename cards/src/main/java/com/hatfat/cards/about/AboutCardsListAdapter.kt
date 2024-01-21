package com.hatfat.cards.about

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import javax.inject.Inject
import javax.inject.Named

class AboutCardsListAdapter @Inject constructor(
    @Named("about resource list")
    private val aboutStringResourceList: List<Int>
) : RecyclerView.Adapter<AboutCardsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutCardsItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_about_cards_item, parent, false)

        val holder = AboutCardsItemViewHolder(view)

        // Make sure all links are followable.
        holder.aboutTextView.movementMethod = LinkMovementMethod.getInstance()

        return holder
    }

    override fun getItemCount(): Int {
        return aboutStringResourceList.size
    }

    override fun onBindViewHolder(holder: AboutCardsItemViewHolder, position: Int) {
        holder.aboutTextView.setText(aboutStringResourceList[position])
    }
}
