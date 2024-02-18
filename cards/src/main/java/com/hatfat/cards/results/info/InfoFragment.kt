package com.hatfat.cards.results.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.glide.CardImageLoader
import com.hatfat.cards.results.SearchResultsRepository
import com.hatfat.cards.results.SearchResultsViewModel
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.cards.results.general.SearchResultsSingleCardState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InfoFragment : Fragment() {

    @Inject
    lateinit var searchResultsDataProvider: SearchResultsDataProvider

    @Inject
    lateinit var cardImageLoader: CardImageLoader

    private val infoListAdapter = InfoListAdapter()

    @Inject
    lateinit var searchResultsRepository: SearchResultsRepository

    private val viewModel: SearchResultsViewModel by hiltNavGraphViewModels(R.id.search_results_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_info, container, false)

        val progressBar = view.findViewById<ProgressBar>(R.id.info_progressbar)
        val infoContainer = view.findViewById<View>(R.id.info_container)

        progressBar.visibility = View.VISIBLE
        infoContainer.visibility = View.GONE

        view?.findViewById<RecyclerView>(R.id.info_recyclerview)?.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = infoListAdapter
        }

        viewModel.searchResultsKey.observe(viewLifecycleOwner) { uuid ->
            searchResultsRepository.getSearchResults(uuid)?.let { searchResults ->
                val position = viewModel.getCurrentPosition()

                val cardData = SearchResultsCardData()
                searchResultsDataProvider.getCardDataForPosition(
                    searchResults,
                    position,
                    cardData
                )

                /* Loading finished, hide spinner and show data */
                progressBar.visibility = View.GONE
                infoContainer.visibility = View.VISIBLE

                infoListAdapter.infoList = cardData.infoList

                view?.findViewById<TextView>(R.id.title_textview)?.apply {
                    this.text = cardData.title
                }

                view?.findViewById<TextView>(R.id.info_list_title)?.apply {
                    this.setText(R.string.info_list_title)
                }

                view?.findViewById<ImageView>(R.id.front_imageview)?.apply {
                    cardImageLoader.loadCardImageUrl(
                        cardData.frontImageUrl,
                        this,
                        cardData.cardBackResourceId
                    )

                    val frontCardData = SearchResultsSingleCardState(
                        position,
                        uuid,
                        isFlipped = false,
                        isRotated = false,
                    )

                    this.setOnClickListener {
                        it?.let {
                            findNavController().navigate(
                                InfoFragmentDirections.actionInfoFragmentToFullscreenCardActivity(
                                    frontCardData
                                )
                            )
                        }
                    }
                }

                view?.findViewById<ImageView>(R.id.back_imageview)?.apply {
                    if (cardData.hasDifferentBack) {
                        cardImageLoader.loadCardImageUrl(
                            cardData.backImageUrl,
                            this,
                            cardData.cardBackResourceId
                        )
                    } else {
                        cardImageLoader.loadCardResourceId(cardData.cardBackResourceId, this)
                    }

                    val backCardData = SearchResultsSingleCardState(
                        position,
                        uuid,
                        isFlipped = true,
                        isRotated = false,
                    )

                    this.setOnClickListener {
                        it?.let {
                            findNavController().navigate(
                                InfoFragmentDirections.actionInfoFragmentToFullscreenCardActivity(
                                    backCardData
                                )
                            )
                        }
                    }
                }
            }
        }

        return view
    }
}