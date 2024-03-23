package com.hatfat.cards.results.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.SearchResultsGraphArgs
import com.hatfat.cards.results.SearchResultsRepository
import com.hatfat.cards.results.SearchResultsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultsListFragment : Fragment() {

    @Inject
    lateinit var searchResultsAdapter: SearchResultsListAdapter

    @Inject
    lateinit var searchResultsRepository: SearchResultsRepository

    private val viewModel: SearchResultsViewModel by hiltNavGraphViewModels(R.id.search_results_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<SearchResultsGraphArgs>().value

        viewModel.setSearchResultsKey(args.resultsKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results_list, container, false)

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val resultsContainer = view.findViewById<ViewGroup>(R.id.search_results_container)
        val linearLayoutManager = LinearLayoutManager(requireContext())

        val recyclerView = view.findViewById<RecyclerView>(R.id.search_results_recyclerview).apply {
            this.layoutManager = linearLayoutManager
            this.adapter = searchResultsAdapter
        }

        /* set to loading state initially */
        progress.visibility = View.VISIBLE
        resultsContainer.visibility = View.GONE

        viewModel.searchResultsKey.observe(viewLifecycleOwner) {
            progress.visibility = View.GONE
            resultsContainer.visibility = View.VISIBLE

            /* set results string and update adapter with search results */
            searchResultsAdapter.searchResults = searchResultsRepository.loadSearchResults(it)
        }

        searchResultsAdapter.onCardSelectedHandler =
            object : SearchResultsListAdapter.OnCardSelectedInterface {
                override fun onCardPressed(position: Int) {
                    viewModel.setCurrentPosition(position)
                    findNavController().navigate(
                        SearchResultsListFragmentDirections.actionSearchResultsListFragmentToSearchResultsCarouselFragment()
                    )
                }
            }

        return view
    }
}