package com.hatfat.cards.results.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultsListFragment : Fragment() {

    @Inject
    lateinit var searchResultsAdapter: SearchResultsListAdapter

    private val viewModel: SearchResultsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<SearchResultsListFragmentArgs>().value

        viewModel.setSearchResults(args.searchResults)

        searchResultsAdapter.onCardSelectedHandler = viewModel
        searchResultsAdapter.onCurrentItemImageHasLoaded =
            object : SearchResultsListAdapter.CurrentItemImageHasLoadedInterface {
                override fun onCurrentItemImageHasLoaded() {
                    Log.e(
                        "catfat",
                        "SearchFragment- Current item is loaded, starting postponed enter transition"
                    )
                    startPostponedEnterTransition()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results_list, container, false)

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val resultsContainer = view.findViewById<ViewGroup>(R.id.search_results_container)
        val searchResultsRecyclerView =
            view.findViewById<RecyclerView>(R.id.search_results_recyclerview).apply {
                this.layoutManager = LinearLayoutManager(requireContext())
                this.adapter = searchResultsAdapter
            }

        /* set to loading state initially */
        progress.visibility = View.VISIBLE
        resultsContainer.visibility = View.GONE

        viewModel.searchResults.observe(viewLifecycleOwner) {
            progress.visibility = View.GONE
            resultsContainer.visibility = View.VISIBLE

            /* set results string and update adapter with search results */
            searchResultsAdapter.searchResults = it
        }

        viewModel.navigateTo.observe(viewLifecycleOwner) {
            it?.let {
                /* Find the image view for the shared element transition */
                val selectedHolder =
                    searchResultsRecyclerView.findViewHolderForAdapterPosition(it.currentPosition) as SearchResultsListViewHolder

                val extras = FragmentNavigatorExtras(
                    selectedHolder.imageView to getSharedImageViewTransitionNameForPosition(it.currentPosition)
                )

                findNavController().navigate(
                    SearchResultsListFragmentDirections.actionSearchResultsListFragmentToSearchResultsSwipeFragment(
                        it
                    ),
                    extras
                )
                viewModel.finishedWithNavigateTo()
            }
        }

        prepareTransitions(searchResultsRecyclerView)
        postponeEnterTransition()

        return view
    }

    private fun prepareTransitions(recyclerView: RecyclerView) {
//        exitTransition = TransitionInflater.from(context)
//            .inflateTransition(R.transition.grid_exit_transition)

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val currentPosition = viewModel.searchResults.value?.currentPosition ?: -1
                val selectedHolder = recyclerView.findViewHolderForAdapterPosition(currentPosition)

                Log.e(
                    "catfat",
                    "onMapSharedElements EXIT: " + currentPosition
                )
                Log.e("catfat", "names: " + names)

                selectedHolder?.let { holder ->
                    sharedElements?.put(
                        names?.get(0) ?: "",
                        holder.itemView.findViewById(R.id.card_imageview)
                    )
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scrollToCurrentPosition()
    }

    /* make sure the item for the currentPosition is visible */
    private fun scrollToCurrentPosition() {
        //catfat IMPLEMENt
        /*
        recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager: RecyclerView.LayoutManager = recyclerView.getLayoutManager()
                val viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPosition)
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    recyclerView.post(Runnable { layoutManager.scrollToPosition(MainActivity.currentPosition) })
                }
            }
        })
         */
    }

    companion object {
        private val sharedImageViewTransitionNamePrefix = "sharedImageViewTransitionName"
        fun getSharedImageViewTransitionNameForPosition(position: Int): String {
            return sharedImageViewTransitionNamePrefix + position
        }
    }
}