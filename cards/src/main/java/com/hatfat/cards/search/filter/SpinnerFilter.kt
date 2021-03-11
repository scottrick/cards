package com.hatfat.cards.search.filter

import java.io.Serializable

open class SpinnerFilter(
    var options: List<String>, /* list of all options in the drop down */
    val notSelectedOption: String?, /* the option in the list which represents not being selected */
    var selectedOption: String? = notSelectedOption /* option that is currently selected */
) : Serializable

//isla  george  daddy  mommy  elouise  peach