package com.hatfat.cards.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hatfat.cards.R
import com.hatfat.cards.results.swipe.SearchResultsSwipeAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InfoFragment : Fragment() {

    @Inject
    lateinit var searchResultsTopAdapter: SearchResultsSwipeAdapter

    @Inject
    lateinit var infoDataProvider: InfoDataProvider

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

        viewModel.infoSelection.observe(viewLifecycleOwner) { infoScreenData ->
            /* Loading finished, hide spinner and show data */
            progressBar.visibility = View.GONE
            infoContainer.visibility = View.VISIBLE

            val data = infoDataProvider.getInfoScreenDataFromSelection(infoScreenData)

            view?.findViewById<TextView>(R.id.title_textview)?.apply {
                this.text = data.title
            }

            view?.findViewById<TextView>(R.id.rules_textview)?.apply {
                this.text = data.rulings.toString()
            }
        }

        return view
    }

    companion object {
        private val TAG = InfoFragment::class.java.simpleName
    }
}
