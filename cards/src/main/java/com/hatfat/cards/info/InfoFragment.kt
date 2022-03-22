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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InfoFragment : Fragment() {

    @Inject
    lateinit var infoDataProvider: InfoDataProvider

    private val infoListAdapter = InfoListAdapter()

    private val viewModel: InfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = navArgs<InfoFragmentArgs>().value

        viewModel.setInfoSelection(args.infoSelection)
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

        viewModel.infoSelection.observe(viewLifecycleOwner) { infoScreenData ->
            /* Loading finished, hide spinner and show data */
            progressBar.visibility = View.GONE
            infoContainer.visibility = View.VISIBLE

            val data = infoDataProvider.getInfoScreenDataFromSelection(infoScreenData)

            infoListAdapter.infoList = data.infoList

            view?.findViewById<TextView>(R.id.title_textview)?.apply {
                this.text = data.title
            }

            view?.findViewById<TextView>(R.id.info_list_title)?.apply {
                this.text = data.infoTitle
            }

            view?.findViewById<ImageView>(R.id.front_imageview)?.apply {
                data.cardFrontImageLoader(this)
            }

            view?.findViewById<ImageView>(R.id.back_imageview)?.apply {
                data.cardBackImageLoader(this)
            }
        }

        return view
    }
}
