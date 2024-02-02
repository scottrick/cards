package com.hatfat.cards.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.results.general.SearchResultsCardData
import com.hatfat.cards.results.general.SearchResultsDataProvider
import com.hatfat.cards.results.general.SearchResultsSingleCardState
import com.hatfat.cards.glide.CardImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InfoFragment : Fragment() {

    @Inject
    lateinit var searchResultsDataProvider: SearchResultsDataProvider

    @Inject
    lateinit var cardImageLoader: CardImageLoader

    private val infoListAdapter = InfoListAdapter()

    private val viewModel: InfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<InfoFragmentArgs>().value

        viewModel.setCardData(args.cardData)
    }

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

        viewModel.infoCardData.observe(viewLifecycleOwner) { singleCardState ->
            /* Loading finished, hide spinner and show data */
            progressBar.visibility = View.GONE
            infoContainer.visibility = View.VISIBLE

            val cardData = SearchResultsCardData()
            searchResultsDataProvider.getCardDataForPosition(
                singleCardState.searchResults,
                singleCardState.position,
                cardData
            )

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
                    singleCardState.position,
                    singleCardState.searchResults,
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
                    singleCardState.position,
                    singleCardState.searchResults,
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

        return view
    }
}