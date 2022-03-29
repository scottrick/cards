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
import com.hatfat.cards.data.card.SingleCardData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InfoFragment : Fragment() {

    @Inject
    lateinit var infoScreenDataProvider: InfoScreenDataProvider

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

        viewModel.infoCardData.observe(viewLifecycleOwner) { infoCardData ->
            /* Loading finished, hide spinner and show data */
            progressBar.visibility = View.GONE
            infoContainer.visibility = View.VISIBLE

            val data = infoScreenDataProvider.getInfoScreenDataFromCard(infoCardData)

            infoListAdapter.infoList = data.infoList

            view?.findViewById<TextView>(R.id.title_textview)?.apply {
                this.text = data.title
            }

            view?.findViewById<TextView>(R.id.info_list_title)?.apply {
                this.text = data.infoTitle
            }

            view?.findViewById<ImageView>(R.id.front_imageview)?.apply {
                data.cardFrontImageLoader(this)

                val frontCardData = SingleCardData(
                    infoCardData.position,
                    infoCardData.searchResults,
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
                data.cardBackImageLoader(this)

                val backCardData = SingleCardData(
                    infoCardData.position,
                    infoCardData.searchResults,
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