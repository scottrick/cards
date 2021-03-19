package com.hatfat.cards.results.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_results_list, container, false)

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val resultsContainer = view.findViewById<ViewGroup>(R.id.search_results_container)
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
                findNavController().navigate(
                    SearchResultsListFragmentDirections.actionSearchResultsListFragmentToSearchResultsSwipeFragment(it)
                )
                viewModel.finishedWithNavigateTo()
            }
        }

        return view
    }
}