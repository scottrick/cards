package com.hatfat.cards.results.swipe

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.hatfat.cards.R
import com.hatfat.cards.app.CardsConfig
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

    @Inject
    lateinit var cardsConfig: CardsConfig

    private val viewModel: SearchResultsSwipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<SearchResultsSwipeFragmentArgs>().value

        viewModel.setSearchResults(args.searchResults)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_search_results_swipe, container, false)

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
            val initialPosition = it.initialPosition
            val lastSelectedPosition = viewModel.lastSelectedPosition.value
            val positionToSelect = lastSelectedPosition ?: initialPosition

            handlePositionSelected(positionToSelect, updateTop = true, updateBottom = true)
        }

        viewModel.isRotated.observe(viewLifecycleOwner) {
            searchResultsTopAdapter.rotated = it
            searchResultsBottomAdapter.rotated = it
        }

        viewModel.isFlipped.observe(viewLifecycleOwner) {
            searchResultsTopAdapter.flipped = it
            searchResultsBottomAdapter.flipped = it
        }

        viewModel.navigateToInfo.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    SearchResultsSwipeFragmentDirections.actionSearchResultsSwipeFragmentToInfoFragment(
                        it
                    )
                )
                viewModel.finishedWithNavigateTo()
            }
        }

        view.findViewById<ImageView>(R.id.rotate_button).setOnClickListener {
            viewModel.rotate()
        }

        /* Show/Hide the FLIP and INFO buttons based on the config */
        view.findViewById<ImageView>(R.id.flip_button)?.apply {
            this.visibility = if (cardsConfig.shouldDisplayFlipButton) View.VISIBLE else View.GONE
        }

        view.findViewById<View>(R.id.info_button)?.apply {
            this.visibility = if (cardsConfig.shouldDisplayInfoButton) View.VISIBLE else View.GONE
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

        view?.findViewById<ImageView>(R.id.flip_button)?.apply {
            val isFlippable = searchResultsBottomAdapter.isFlippable(position)
            this.isEnabled = isFlippable

            this.setOnClickListener {
                viewModel.flip()
            }
        }

        view?.findViewById<ImageView>(R.id.info_button)?.apply {
            val hasRulings = searchResultsBottomAdapter.hasRulings(position)
            this.isEnabled = hasRulings

            this.setOnClickListener {
                viewModel.infoPressed(position)
            }
        }

        view?.findViewById<ImageView>(R.id.share_button)?.setOnClickListener {
            searchResultsBottomAdapter.handleSharePressed(position)
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