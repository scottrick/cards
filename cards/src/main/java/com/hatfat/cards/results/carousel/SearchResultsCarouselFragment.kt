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
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.app.CardsConfig
import com.hatfat.cards.results.SearchResultsRepository
import com.hatfat.cards.results.SearchResultsViewModel
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

    @Inject
    lateinit var searchResultsRepository: SearchResultsRepository

    private val viewModel: SearchResultsViewModel by hiltNavGraphViewModels(R.id.search_results_graph)

    private val snapHelper = PagerSnapHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_search_results_carousel, container, false)

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val resultsContainer = view.findViewById<ViewGroup>(R.id.search_results_container)
        val resultsInfoTextView =
            view.findViewById<TextView>(R.id.search_results_total_number_textview)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        val prominentLayoutManager = ProminentLayoutManager(requireContext())
        prominentLayoutManager.setProminentChildChangedListener(object :
            ProminentLayoutManager.ProminentChildChangedListener {
            override fun prominentChildChanged() {
                prominentLayoutManager.getProminentChild()?.also {
                    handlePositionSelected(recyclerView.getChildAdapterPosition(it))
                }
            }
        })

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
        }

        /* set to loading state initially */
        progress.visibility = View.VISIBLE
        resultsContainer.visibility = View.GONE

        viewModel.searchResultsKey.observe(viewLifecycleOwner) { uuid ->
            searchResultsRepository.loadSearchResults(uuid)?.let { searchResults ->
                progress.visibility = View.GONE
                resultsContainer.visibility = View.VISIBLE

                /* set results string and update adapter with search results */
                resultsInfoTextView.text =
                    resources.getQuantityString(
                        R.plurals.number_of_search_results,
                        searchResults.size,
                        searchResults.size
                    )

                searchResultsCarouselAdapter.searchResults = searchResults

                /* select the last position selected */
                initRecyclerViewPosition(
                    recyclerView,
                    prominentLayoutManager,
                    viewModel.getCurrentPosition()
                )
            }
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
                    SearchResultsCarouselFragmentDirections.actionSearchResultsCarouselFragmentToInfoFragment()
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
        // Update fragment result, so the search results list can make sure the latest selected item is visible.
        setFragmentResult(LAST_SELECTED_POSITION_KEY, bundleOf(POSITION_KEY to position))

        val cardData = SearchResultsCardData()
        searchResultsRepository.loadSearchResults(viewModel.searchResultsKey.value)
            ?.let { searchResults ->
                searchResultsDataProvider.getCardDataForPosition(searchResults, position, cardData)
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

        view?.findViewById<TextView>(R.id.search_results_info_line_1)?.apply {
            this.text = cardData.carouselInfoText1
        }
        view?.findViewById<TextView>(R.id.search_results_info_line_2)?.apply {
            this.text = cardData.carouselInfoText2
        }
        view?.findViewById<TextView>(R.id.search_results_info_line_3)?.apply {
            this.text = cardData.carouselInfoText3
        }

        val displayPosition = position + 1
        view?.findViewById<TextView>(R.id.search_results_current_index_textview)?.apply {
            this.text = displayPosition.toString()
        }

        viewModel.setCurrentPosition(position)
    }

    private fun initRecyclerViewPosition(
        recyclerView: RecyclerView,
        prominentLayoutManager: ProminentLayoutManager,
        position: Int
    ) {
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
        if (!isAdded) {
            // Only attempt to share if we are still attached to an activity
            return
        }

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

        const val LAST_SELECTED_POSITION_KEY = "LAST_SELECTED_POSITION_KEY"
        const val POSITION_KEY = "POSITION_KEY"
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