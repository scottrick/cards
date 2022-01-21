package com.hatfat.cards.results.swipe

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.SharedElementCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.transition.TransitionInflater
import com.hatfat.cards.R
import com.hatfat.cards.results.list.SearchResultsListViewHolder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultsSwipeFragment : Fragment() {

    @Inject
    lateinit var searchResultsBottomAdapter: SearchResultsSwipeAdapter

    @Inject
    lateinit var searchResultsTopAdapter: SearchResultsSwipeAdapter

    private val viewModel: SearchResultsSwipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<SearchResultsSwipeFragmentArgs>().value

        viewModel.setSearchResults(args.searchResults)
        Log.e("catfat", "onCreate swipe fragment currentItem: " + args.searchResults.currentPosition)

        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(R.transition.shared_image);
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext()).inflateTransition(R.transition.shared_image);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results_swipe, container, false)

        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val orientation = if (isLandscape) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL

        searchResultsTopAdapter.isFullscreen = false
        searchResultsTopAdapter.isLandscape = isLandscape
        searchResultsTopAdapter.onCardSelectedHandler =
            object : SearchResultsSwipeAdapter.OnCardSelectedInterface {
                override fun onCardPressed(position: Int) {
                    handlePositionSelected(position, updateTop = false, updateBottom = true)
                }

                override fun onCardLongPressed(position: Int) {
                    /* long pressed top card, don't care about that */
                }
            }
        searchResultsBottomAdapter.isFullscreen = true
        searchResultsBottomAdapter.isLandscape = isLandscape
        searchResultsBottomAdapter.onCardSelectedHandler =
            object : SearchResultsSwipeAdapter.OnCardSelectedInterface {
                override fun onCardPressed(position: Int) {
                    handlePositionSelected(position, true, updateBottom = false)
                }

                override fun onCardLongPressed(position: Int) {
                    searchResultsBottomAdapter.handleLongPress(position)
                }
            }

        searchResultsBottomAdapter.shareCardBitmapInterface =
            object : SearchResultsSwipeAdapter.ShareCardBitmapInterface {
                override fun shareBitmap(bitmap: Bitmap) {
                    shareCardBitmap(bitmap)
                }
            }

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val resultsContainer = view.findViewById<ViewGroup>(R.id.search_results_container)
        val resultsInfoTextView = view.findViewById<TextView>(R.id.search_results_info_textview)

        view.findViewById<RecyclerView>(R.id.top_recycler_view).apply {
            this.layoutManager = LinearLayoutManager(context, orientation, false)
            this.adapter = searchResultsTopAdapter
        }

        view.findViewById<RecyclerView>(R.id.bottom_recycler_view).apply {
            this.layoutManager = LinearLayoutManager(context, orientation, false)
            this.adapter = searchResultsBottomAdapter

            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(this)

            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when (newState) {
                        SCROLL_STATE_IDLE -> {
                            val firstPosition =
                                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            handlePositionSelected(
                                firstPosition,
                                updateTop = true,
                                updateBottom = false
                            )
                        }
                    }
                }
            })

            setEnterSharedElementCallback(object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: MutableList<String>?,
                    sharedElements: MutableMap<String, View>?
                ) {
                    val currentPosition = this@SearchResultsSwipeFragment.viewModel.searchResults.value?.currentPosition ?: - 1
//                    val currentPosition = viewModel.searchResults.value?.currentPosition ?: 0
                    Log.e("catfat", "onMapSharedElements ENTER: " + currentPosition)
                    val selectedHolder =
                        this@apply.findViewHolderForAdapterPosition(currentPosition) as SearchResultsSwipeViewHolder?

                    Log.e("catfat", "names: " + names)

                    selectedHolder?.let { holder ->
                        sharedElements?.put(
                            names?.get(0) ?: "",
                            holder.imageView
                        )
                    }
                }
            })
        }

        /* set to loading state initially */
        progress.visibility = View.VISIBLE
        resultsContainer.visibility = View.GONE

        viewModel.searchResults.observe(viewLifecycleOwner) {
            progress.visibility = View.GONE
            resultsContainer.visibility = View.VISIBLE

            /* set results string and update adapter with search results */
            resultsInfoTextView.text =
                resources.getQuantityString(R.plurals.number_of_search_results, it.size, it.size)
            searchResultsTopAdapter.searchResults = it
            searchResultsBottomAdapter.searchResults = it

            /* select the last position selected */
            val initialPosition = it.currentPosition
            val lastSelectedPosition = viewModel.lastSelectedPosition.value
            val positionToSelect = lastSelectedPosition ?: initialPosition

            handlePositionSelected(positionToSelect, updateTop = true, updateBottom = true)
        }

        searchResultsBottomAdapter.cardImageLoadedInterface =
            object : SearchResultsSwipeAdapter.CardImageLoadedInterface {
                override fun cardImageIsLoaded() {
                    startPostponedEnterTransition()
                }
            }

        viewModel.isRotated.observe(viewLifecycleOwner, {
            searchResultsTopAdapter.rotated = it
            searchResultsBottomAdapter.rotated = it
        })

        viewModel.isFlipped.observe(viewLifecycleOwner, {
            searchResultsTopAdapter.flipped = it
            searchResultsBottomAdapter.flipped = it
        })

        view.findViewById<Button>(R.id.rotate_button).setOnClickListener {
            viewModel.rotate()
        }

        view?.findViewById<Button>(R.id.flip_button)?.setOnClickListener {
            viewModel.flip()
        }

        return view
    }

    private fun handlePositionSelected(position: Int, updateTop: Boolean, updateBottom: Boolean) {
        if (updateTop) {
            view?.findViewById<RecyclerView>(R.id.top_recycler_view)?.apply {
                this.scrollToPosition(position)
            }
        }

        if (updateBottom) {
            view?.findViewById<RecyclerView>(R.id.bottom_recycler_view)?.apply {
                this.scrollToPosition(position)
            }
        }

        view?.findViewById<Button>(R.id.flip_button)?.apply {
            val isFlippable = searchResultsBottomAdapter.isFlippable(position)
            this.visibility = if (isFlippable) View.VISIBLE else View.INVISIBLE
        }

        view?.findViewById<TextView>(R.id.search_results_info_extra)?.apply {
            this.text = searchResultsBottomAdapter.extraText(position)
        }

        viewModel.updateLastSelectedPosition(position)
    }

    private fun shareCardBitmap(bitmap: Bitmap) {
        val cachePath = File(requireContext().externalCacheDir, "shared_card_images/")
        cachePath.mkdirs()

        val imageFile = File(cachePath, "share_card_image.png")
        val fileOutputStream: FileOutputStream

        try {
            fileOutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            Log.e(TAG, "Exception sharing card: $e")
        }

        val sharedImageUri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().packageName + ".provider",
            imageFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.putExtra(Intent.EXTRA_STREAM, sharedImageUri)
        shareIntent.type = "image/png"
        startActivity(shareIntent)
    }

    companion object {
        private val TAG = SearchResultsSwipeFragment::class.java.simpleName
    }
}