package com.hatfat.cards.results.swipe

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.hatfat.cards.R
import dagger.hilt.android.AndroidEntryPoint
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_results_swipe, container, false)

        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val orientation = if (isLandscape) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL
        val onCardSelectedHandler = object : SearchResultsSwipeAdapter.OnCardSelectedInterface {
            override fun onCardPressed(position: Int) {
                handlePositionSelected(position)
            }
        }

        searchResultsTopAdapter.isFullscreen = false
        searchResultsTopAdapter.isLandscape = isLandscape
        searchResultsTopAdapter.onCardSelectedHandler = onCardSelectedHandler
        searchResultsBottomAdapter.isFullscreen = true
        searchResultsBottomAdapter.isLandscape = isLandscape
        searchResultsBottomAdapter.onCardSelectedHandler = onCardSelectedHandler

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val resultsContainer = view.findViewById<ViewGroup>(R.id.search_results_container)
        val resultsInfoTextView = view.findViewById<TextView>(R.id.search_results_info_textview)

        val topRecyclerView = view.findViewById<RecyclerView>(R.id.top_recycler_view).apply {
            this.layoutManager = LinearLayoutManager(context, orientation, false)
            this.adapter = searchResultsTopAdapter
        }

        val bottomRecyclerView = view.findViewById<RecyclerView>(R.id.bottom_recycler_view).apply {
            this.layoutManager = LinearLayoutManager(context, orientation, false)
            this.adapter = searchResultsBottomAdapter

            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(this)

            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when (newState) {
                        SCROLL_STATE_IDLE -> {
                            val firstPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            handlePositionSelected(firstPosition)
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
            resultsInfoTextView.text = getString(R.string.search_results_count, it.size)
            searchResultsTopAdapter.searchResults = it
            searchResultsBottomAdapter.searchResults = it

            handlePositionSelected(it.initialPosition)
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

    private fun handlePositionSelected(position: Int) {
        view?.findViewById<RecyclerView>(R.id.top_recycler_view)?.apply {
            this.scrollToPosition(position)
        }

        view?.findViewById<RecyclerView>(R.id.bottom_recycler_view)?.apply {
            this.scrollToPosition(position)
        }

        view?.findViewById<Button>(R.id.flip_button)?.apply {
            val isFlippable = searchResultsBottomAdapter.isFlippable(position)
            this.visibility = if (isFlippable) View.VISIBLE else View.INVISIBLE
        }

        view?.findViewById<TextView>(R.id.search_results_info_extra)?.apply {
            this.text = searchResultsBottomAdapter.extraText(position)
        }
    }
}