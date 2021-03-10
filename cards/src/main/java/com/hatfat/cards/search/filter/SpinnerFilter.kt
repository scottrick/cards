package com.hatfat.cards.search.filter

import java.io.Serializable

open class SpinnerFilter(
    val liveDataKey: String,
    val options: List<String>, /* list of all options in the drop down */
    val notSelectedIndex: Int, /* the option index which represents not being selected */
    var selectedIndex: Int = notSelectedIndex /* option that is currently selected */
) : Serializable

//isla  george  daddy  mommy  elouise  peach