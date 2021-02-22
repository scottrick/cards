package com.hatfat.cards.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.hatfat.cards.R
import com.hatfat.cards.base.DataReady
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CardsSearchFragment : Fragment() {

    @Inject
    lateinit var isDataReady: DataReady

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cards_search, container, false)

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)

        val testSpinner = view.findViewById<Spinner>(R.id.test_spinner)
        val spinnerArray = mutableListOf("tesafdsafd", "asdfadsfvcxzcv")

        isDataReady.isDataReady.observe(viewLifecycleOwner) {
            //catfat MOVE THIS LOGIC into view model, can add simple SHOW/HIDE loading spinner livedata that the fragment watches
            progress.visibility = if (it) View.GONE else View.VISIBLE
            testSpinner.visibility = if (it) View.VISIBLE else View.GONE
        }

        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerArray
        )

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        testSpinner.adapter = spinnerArrayAdapter

        /*
        val button = view.findViewById<Button>(R.id.test_button)

        button.setOnClickListener {
            val directions = CardsSearchFragmentDirections.actionCardSearchFragmentToCardListFragment()
            findNavController().navigate(directions)
        }
         */

        return view
    }
}