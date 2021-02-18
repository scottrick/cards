package com.hatfat.cards.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.hatfat.cards.R


class CardsSearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cards_search, container, false)

        val testSpinner = view.findViewById<Spinner>(R.id.test_spinner)
        val spinnerArray = mutableListOf("tesafdsafd", "asdfadsfvcxzcv")

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