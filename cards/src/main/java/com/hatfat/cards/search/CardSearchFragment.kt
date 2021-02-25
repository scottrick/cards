package com.hatfat.cards.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hatfat.cards.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardSearchFragment : Fragment() {

    private val viewModel: CardSearchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_card_search, container, false)
        val layoutInflater = LayoutInflater.from(view.context)

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val resetButton = view.findViewById<Button>(R.id.reset_button)
        val searchButton = view.findViewById<Button>(R.id.search_button)
        val searchContainer = view.findViewById<ViewGroup>(R.id.search_scrollview)
        val searchStringEditText = view.findViewById<EditText>(R.id.search_edittext)
        val textSearchOptionsContainer = view.findViewById<ViewGroup>(R.id.text_search_options_container)

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                CardSearchViewModel.State.ENTERING_INFO -> {
                    searchContainer.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                }
                CardSearchViewModel.State.LOADING -> {
                    searchContainer.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                }
                CardSearchViewModel.State.SEARCHING -> {
                    searchContainer.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                }
            }
        }

        viewModel.basicTextSearchOptions.forEach { searchOptionLiveData ->
            val checkBox = layoutInflater.inflate(R.layout.search_checkbox, textSearchOptionsContainer, false) as CheckBox
            searchOptionLiveData.observe(viewLifecycleOwner) {
                checkBox.setText(it.searchOptionStringResourceId)
                checkBox.isChecked = it.isEnabled
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.textSearchOptionCheckedChanged(it, isChecked)
                }
            }

            textSearchOptionsContainer.addView(checkBox)
        }

        viewModel.searchTextEnabled.observe(viewLifecycleOwner, Observer {
            searchStringEditText.isEnabled = it
        })

        viewModel.searchString.observe(viewLifecycleOwner, Observer {
            if (!searchStringEditText.text.toString().equals(it)) {
                searchStringEditText.setText(it)
            }
        })

        searchStringEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchString(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        searchStringEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                return when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        viewModel.searchPressed()
                        true
                    }
                    else -> false
                }
            }
        })

        resetButton.setOnClickListener { viewModel.resetPressed() }
        searchButton.setOnClickListener { viewModel.searchPressed() }

        viewModel.searchResults.observe(viewLifecycleOwner) {
            it?.let {
                if (it.size <= 0) {
                    Toast.makeText(requireContext(), R.string.search_no_results_toast, Toast.LENGTH_SHORT).show()
                } else {
                    findNavController().navigate(
                        CardSearchFragmentDirections.actionCardSearchFragmentToSearchResultsListFragment(
                            it
                        )
                    )
                }

                viewModel.finishedWithSearchResults()
            }
        }

        return view
    }
}