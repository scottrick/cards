package com.hatfat.cards.results.carousel

import android.content.Intent
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
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.app.CardsConfig
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

// https://medium.com/holler-developers/paging-image-gallery-with-recyclerview-f059d035b7e7
@AndroidEntryPoint
class SearchResultsCarouselFragment : Fragment() {

    @Inject
    lateinit var searchResultsCarouselAdapter: SearchResultsCarouselAdapter

    @Inject
    lateinit var cardsConfig: CardsConfig

    @Inject
    lateinit var searchResultsDataProvider: SearchResultsDataProvider

    private val viewModel: SearchResultsCarouselViewModel by viewModels()

    private val snapHelper = PagerSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<SearchResultsCarouselFragmentArgs>().value

        viewModel.setSearchResults(args.searchResults)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_search_results_carousel, container, false)

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val resultsContainer = view.findViewById<ViewGroup>(R.id.search_results_container)
        val resultsInfoTextView = view.findViewById<TextView>(R.id.search_results_info_textview)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        val prominentLayoutManager = ProminentLayoutManager(requireContext())

        searchResultsCarouselAdapter.onCardSelectedHandler =
            object : SearchResultsCarouselAdapter.OnCardSelectedInterface {
                override fun onCardPressed(position: Int) {
                    prominentLayoutManager.getProminentChild()?.also {
                        val prominentChildPosition = recyclerView.getChildAdapterPosition(it)
                        if (prominentChildPosition == position) {
                            // Pressed prominent view, so make it big!
                            viewModel.bigCardPressed(
                                position,
                                viewModel.isFlipped.value ?: false,
                                viewModel.isRotated.value ?: false
                            )
                        } else {
                            // Pressed side view, so scroll to it.
                            recyclerView.smoothScrollToCenteredPosition(position)
                        }
                    }

                }
            }

        searchResultsCarouselAdapter.shareCardBitmapInterface =
            object : SearchResultsCarouselAdapter.ShareCardBitmapInterface {
                override fun shareBitmap(cardTitle: String?, bitmap: Bitmap) {
                    shareCardBitmap(cardTitle, bitmap)
                }
            }

        recyclerView.apply {
            setItemViewCacheSize(4)
            this.layoutManager = prominentLayoutManager
            this.adapter = searchResultsCarouselAdapter

            val spacing = resources.getDimensionPixelSize(R.dimen.carousel_spacing)
            addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
            addItemDecoration(BoundsOffsetDecoration())

            snapHelper.attachToRecyclerView(this)

            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> {
                            prominentLayoutManager.getProminentChild()?.also {
                                handlePositionSelected(recyclerView.getChildAdapterPosition(it))
                            }
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
            searchResultsCarouselAdapter.searchResults = it

            /* select the last position selected */
            val initialPosition = it.initialPosition
            val lastSelectedPosition = viewModel.lastSelectedPosition.value
            val positionToSelect = lastSelectedPosition ?: initialPosition

            initRecyclerViewPosition(recyclerView, prominentLayoutManager, positionToSelect)
        }

        viewModel.isRotated.observe(viewLifecycleOwner) {
            searchResultsCarouselAdapter.rotated = it
        }

        viewModel.isFlipped.observe(viewLifecycleOwner) {
            searchResultsCarouselAdapter.flipped = it
        }

        viewModel.navigateToInfo.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    SearchResultsCarouselFragmentDirections.actionSearchResultsCarouselFragmentToInfoFragment(
                        it
                    )
                )
                viewModel.finishedWithNavigate()
            }
        }

        viewModel.navigateToFullscreen.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    SearchResultsCarouselFragmentDirections.actionSearchResultsCarouselFragmentToFullscreenCardActivity(
                        it
                    )
                )
                viewModel.finishedWithNavigate()
            }
        }

        view.findViewById<ImageView>(R.id.rotate_button).setOnClickListener {
            viewModel.rotate()
        }

        /* only hide both optional buttons if both aren't enabled */
        val missingVisibility =
            if (cardsConfig.shouldDisplayFlipButton || cardsConfig.shouldDisplayInfoButton) View.VISIBLE else View.GONE

        /* Show/Hide the FLIP and INFO buttons based on the config */
        view.findViewById<ImageView>(R.id.flip_button)?.apply {
            this.visibility =
                if (cardsConfig.shouldDisplayFlipButton) View.VISIBLE else missingVisibility
        }

        view.findViewById<View>(R.id.info_button)?.apply {
            this.visibility =
                if (cardsConfig.shouldDisplayInfoButton) View.VISIBLE else missingVisibility
        }

        return view
    }

    private fun handlePositionSelected(position: Int) {
        val cardData = SearchResultsCardData()
        viewModel.searchResults.value?.let {
            searchResultsDataProvider.getCardDataForPosition(it, position, cardData)
        }

        view?.findViewById<ImageView>(R.id.flip_button)?.apply {
            this.isEnabled = cardData.hasDifferentBack

            this.setOnClickListener {
                viewModel.flip()
            }
        }

        view?.findViewById<ImageView>(R.id.info_button)?.apply {
            this.isEnabled = cardData.hasExtraInfo()

            this.setOnClickListener {
                viewModel.infoPressed(
                    position,
                    viewModel.isFlipped.value ?: false,
                    viewModel.isRotated.value ?: false
                )
            }
        }

        view?.findViewById<ImageView>(R.id.share_button)?.setOnClickListener {
            searchResultsCarouselAdapter.handleSharePressed(position)
        }

        view?.findViewById<TextView>(R.id.search_results_info_extra)?.apply {
            this.text = cardData.carouselExtraText
        }

        viewModel.updateLastSelectedPosition(position)
    }

    private fun initRecyclerViewPosition(
        recyclerView: RecyclerView,
        prominentLayoutManager: ProminentLayoutManager,
        position: Int
    ) {
        Log.e(TAG, "init recycler view position: $position")
        prominentLayoutManager.scrollToPosition(position)

        recyclerView.doOnPreDraw {
            val targetView =
                prominentLayoutManager.findViewByPosition(position) ?: return@doOnPreDraw
            val distanceToFinalSnap =
                snapHelper.calculateDistanceToFinalSnap(prominentLayoutManager, targetView)
                    ?: return@doOnPreDraw

            prominentLayoutManager.scrollToPositionWithOffset(position, -distanceToFinalSnap[0])
        }

        handlePositionSelected(position)
    }

    private fun shareCardBitmap(cardTitle: String?, bitmap: Bitmap) {
        val cachePath = File(requireContext().externalCacheDir, "shared_card_images/")
        cachePath.mkdirs()

        val filename = cardTitle ?: System.currentTimeMillis().toString()
        val imageFile = File(cachePath, "$filename.png")
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
        private val TAG = SearchResultsCarouselFragment::class.java.simpleName
    }
}

// Helper to animate to another position in the carousel.
private fun RecyclerView.smoothScrollToCenteredPosition(position: Int) {
    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun calculateDxToMakeVisible(view: View?, snapPreference: Int): Int {
            val dxToStart = super.calculateDxToMakeVisible(view, SNAP_TO_START)
            val dxToEnd = super.calculateDxToMakeVisible(view, SNAP_TO_END)

            return (dxToStart + dxToEnd) / 2
        }
    }

    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}