package com.hatfat.cards.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hatfat.cards.R
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.SpinnerOption
import com.hatfat.cards.util.hideKeyboard
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
        val dropDownOptionsParentContainerLabel = view.findViewById<View>(R.id.dropdown_options_parent_container_label)
        val dropDownOptionsParentContainer = view.findViewById<ViewGroup>(R.id.dropdown_options_parent_container)
        val dropDownOptionsContainer = view.findViewById<ViewGroup>(R.id.dropdown_options_container)

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                CardSearchViewModel.State.ENTERING_INFO -> {
                    searchContainer.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                }
                CardSearchViewModel.State.LOADING,
                CardSearchViewModel.State.SEARCHING -> {
                    searchContainer.visibility = View.GONE
                    progress.visibility = View.VISIBLE
                    dismissKeyboard()
                }
                else -> {
                    Log.e("CardSearchFragment", "Invalid state, ignoring: $it")
                }
            }
        }

        viewModel.textSearchFilters.forEach { searchOptionLiveData ->
            val checkBox = layoutInflater.inflate(R.layout.search_checkbox, textSearchOptionsContainer, false) as CheckBox
            searchOptionLiveData.observe(viewLifecycleOwner) {
                checkBox.text = it.textFilterName
                checkBox.isChecked = it.isEnabled
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.textSearchOptionCheckedChanged(it, isChecked)
                }
            }

            textSearchOptionsContainer.addView(checkBox)
        }

        val dropDownFilters = viewModel.spinnerFilters
        if (dropDownFilters.isEmpty()) {
            /* no dropdown filters, so hide! */
            dropDownOptionsParentContainerLabel.visibility = GONE
            dropDownOptionsParentContainer.visibility = GONE
        } else {
            dropDownOptionsParentContainerLabel.visibility = VISIBLE
            dropDownOptionsParentContainer.visibility = VISIBLE

            var i = 0
            while (i < dropDownFilters.size) {
                val filterOne = dropDownFilters[i]
                val filterTwo = if (i + 1 < dropDownFilters.size) dropDownFilters[i + 1] else null

                val dropDownRow = layoutInflater.inflate(R.layout.search_dropdown_row, dropDownOptionsContainer, false) as LinearLayout
                val spinner1 = dropDownRow.findViewById<Spinner>(R.id.spinner1)
                val spinner2 = dropDownRow.findViewById<Spinner>(R.id.spinner2)

                setupSpinnerForLiveData(filterOne, spinner1)
                if (filterTwo == null) {
                    spinner2.visibility = INVISIBLE
                } else {
                    spinner2.visibility = VISIBLE
                    setupSpinnerForLiveData(filterTwo, spinner2)
                }

                dropDownOptionsContainer.addView(dropDownRow)

                i += 2
            }
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

    private fun setupSpinnerForLiveData(filterLiveData: LiveData<SpinnerFilter>, spinner: Spinner) {
        val spinnerAdapter = ArrayAdapter(spinner.context, R.layout.search_dropdown_item, ArrayList<String>())
        spinnerAdapter.setDropDownViewResource(R.layout.search_dropdown_item)
        spinner.adapter = spinnerAdapter

        filterLiveData.observe(viewLifecycleOwner) { spinnerFilter ->
            spinnerAdapter.clear()
            spinnerAdapter.addAll(spinnerFilter.options.map { it.displayName })
            val selectedIndex =
                if (spinnerFilter.options.contains(spinnerFilter.selectedOption)) spinnerFilter.options.indexOf(spinnerFilter.selectedOption) else 0
            spinner.setSelection(selectedIndex)
        }

        /* handle spinner changing here */
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val dropDownFilter = filterLiveData.value ?: return
                dropDownFilter.selectedOption = dropDownFilter.options[position]
                viewModel.dropDownFilterSelectionChanged(dropDownFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                /* do nothing, we don't care */
            }
        }
    }

    override fun onPause() {
        super.onPause()
        dismissKeyboard()
    }

    private fun dismissKeyboard() {
        view?.hideKeyboard()
    }
}