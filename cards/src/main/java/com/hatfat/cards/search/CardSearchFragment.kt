package com.hatfat.cards.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hatfat.cards.R
import com.hatfat.cards.search.filter.SpinnerFilter
import com.hatfat.cards.search.filter.advanced.AdvancedFilterAdapter
import com.hatfat.cards.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardSearchFragment : Fragment() {

    private val viewModel: CardSearchViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        viewModel.handleOnStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card_search, container, false)
        val layoutInflater = LayoutInflater.from(view.context)

        val progress = view.findViewById<ProgressBar>(R.id.search_progressbar)
        val searchContainer = view.findViewById<ViewGroup>(R.id.search_scrollview)
        val searchStringEditText = view.findViewById<EditText>(R.id.search_edittext)
        val textSearchOptionsContainer =
            view.findViewById<ViewGroup>(R.id.text_search_options_container)
        val dropDownOptionsParentContainerLabel =
            view.findViewById<View>(R.id.dropdown_options_parent_container_label)
        val dropDownOptionsParentContainer =
            view.findViewById<ViewGroup>(R.id.dropdown_options_parent_container)
        val dropDownOptionsContainer = view.findViewById<ViewGroup>(R.id.dropdown_options_container)
        val advancedFilterContainerLabel =
            view.findViewById<View>(R.id.advanced_filter_container_label)
        val advancedFilterContainer = view.findViewById<View>(R.id.advanced_filter_container)
        val advancedFilterStatusTextView =
            view.findViewById<TextView>(R.id.advanced_filter_status_textview)
        val addAdvancedFilterButton = view.findViewById<View>(R.id.advanced_filter_add_imageview)

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                CardSearchViewModel.State.ENTERING_INFO -> {
                    searchContainer.visibility = VISIBLE
                    progress.visibility = GONE
                }

                CardSearchViewModel.State.LOADING,
                CardSearchViewModel.State.SEARCH_FINISHED,
                CardSearchViewModel.State.SEARCHING -> {
                    searchContainer.visibility = GONE
                    progress.visibility = VISIBLE
                    dismissKeyboard()
                }

                else -> {
                    Log.e("CardSearchFragment", "Invalid state, ignoring: $it")
                }
            }
        }

        viewModel.textSearchFilters.forEach { searchOptionLiveData ->
            val checkBox = layoutInflater.inflate(
                R.layout.search_checkbox,
                textSearchOptionsContainer,
                false
            ) as CheckBox
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

                val dropDownRow = layoutInflater.inflate(
                    R.layout.search_dropdown_row,
                    dropDownOptionsContainer,
                    false
                ) as LinearLayout
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

        viewModel.searchTextEnabled.observe(viewLifecycleOwner) {
            searchStringEditText.isEnabled = it
        }

        viewModel.searchString.observe(viewLifecycleOwner) {
            if (searchStringEditText.text.toString() != it) {
                searchStringEditText.setText(it)
            }
        }

        searchStringEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchString(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        searchStringEditText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    viewModel.searchPressed()
                    true
                }

                else -> false
            }
        }

        view.findViewById<RecyclerView>(R.id.advanced_options_recyclerview).apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            val advancedFilterAdapter = AdvancedFilterAdapter()
            advancedFilterAdapter.handler = viewModel
            this.adapter = advancedFilterAdapter
            viewModel.advancedFilters.observe(viewLifecycleOwner) {
                advancedFilterAdapter.filters = it
                advancedFilterStatusTextView.text = resources.getQuantityString(
                    R.plurals.number_of_advanced_filters,
                    it.size,
                    it.size
                )
            }
        }

        if (viewModel.hasAdvancedFilters) {
            advancedFilterContainer.visibility = VISIBLE
            advancedFilterContainerLabel.visibility = VISIBLE
        } else {
            advancedFilterContainer.visibility = GONE
            advancedFilterContainerLabel.visibility = GONE
        }

        viewModel.isAddCustomFilterEnabled.observe(viewLifecycleOwner) {
            if (it) {
                addAdvancedFilterButton.setBackgroundResource(R.drawable.primary_color_button_background)
                addAdvancedFilterButton.isEnabled = true
            } else {
                addAdvancedFilterButton.setBackgroundResource(R.color.colorPrimaryVariant)
                addAdvancedFilterButton.isEnabled = false
            }
        }

        addAdvancedFilterButton.setOnClickListener { viewModel.newAdvancedFilterPressed() }

        viewModel.searchResults.observe(viewLifecycleOwner) {
            it?.let { uuid ->
                findNavController().navigate(
                    CardSearchFragmentDirections.actionCardSearchFragmentToSearchResultsListFragment(
                        uuid
                    )
                )

                viewModel.finishedWithSearchResults()
            }
        }

        viewModel.noSearchResults.observe(viewLifecycleOwner) { noResults ->
            if (noResults) {
                Toast.makeText(
                    requireContext(),
                    R.string.search_no_results_toast,
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.finishedWithSearchResults()
            }
        }

        return view
    }

    private fun setupSpinnerForLiveData(filterLiveData: LiveData<SpinnerFilter>, spinner: Spinner) {
        val spinnerAdapter =
            ArrayAdapter(spinner.context, R.layout.view_dropdown_item, ArrayList<String>())
        spinnerAdapter.setDropDownViewResource(R.layout.view_dropdown_item)
        spinner.adapter = spinnerAdapter

        filterLiveData.observe(viewLifecycleOwner) { spinnerFilter ->
            spinnerAdapter.clear()
            spinnerAdapter.addAll(spinnerFilter.options.map { it.displayName })
            val selectedIndex =
                if (spinnerFilter.options.contains(spinnerFilter.selectedOption)) spinnerFilter.options.indexOf(
                    spinnerFilter.selectedOption
                ) else 0
            spinner.setSelection(selectedIndex)
        }

        /* handle spinner changing here */
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
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