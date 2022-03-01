package com.hatfat.cards.search.filter

import java.io.Serializable

open class SpinnerFilter(
    var options: List<SpinnerOption>, /* list of all options in the drop down */
    val notSelectedOption: SpinnerOption?, /* the option in the list which represents not being selected */
    val defaultOption: SpinnerOption? = notSelectedOption, /* the option in the list which is selected by default */
    var selectedOption: SpinnerOption? = defaultOption /* option that is currently selected */
) : Serializable