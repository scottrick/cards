package com.hatfat.cards.results.list

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class SearchResultsListGlideRequestListener(
    private val itemLoadedInterface: SearchResultsListAdapter.ItemImageHasLoadedInterface,
    private val position: Int
) : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        itemLoadedInterface.onItemImageHasLoaded(position)
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        itemLoadedInterface.onItemImageHasLoaded(position)
        return false
    }
}