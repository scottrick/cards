package com.hatfat.cards.search.filter.advanced

import java.io.Serializable

open class AdvancedFilter(
    open val fields: List<AdvancedFilterField>,
    open val modes: List<AdvancedFilterMode>,
    var selectedFieldIndex: Int = 0,
    var selectedModeIndex: Int = 0,
    var inputValue: String = ""
) : Serializable {

    /* for the given fieldValue, determines if that value passes the filter */
    fun fieldFilter(fieldValue: String): Boolean {
        return when (val mode = modes[selectedModeIndex]) {
            AdvancedFilterMode.CONTAINS -> {
                /* treat as strings */
                fieldValue.contains(inputValue, true)
            }
            AdvancedFilterMode.DOES_NOT_CONTAIN -> {
                /* treat as strings */
                !fieldValue.contains(inputValue, true)
            }
            AdvancedFilterMode.EQUALS -> {
                /* treat as strings */
                fieldValue.compareTo(inputValue, true) == 0
            }
            AdvancedFilterMode.NOT_EQUALS -> {
                /* treat as strings */
                fieldValue.compareTo(inputValue, true) != 0
            }
            AdvancedFilterMode.LESS_THAN,
            AdvancedFilterMode.LESS_THAN_OR_EQUAL,
            AdvancedFilterMode.GREATER_THAN_OR_EQUAL,
            AdvancedFilterMode.GREATER_THAN -> {
                /* treat as numbers */
                val fieldInt = fieldValue.toIntOrNull()
                val inputInt = inputValue.toIntOrNull()
                if (fieldInt == null || inputInt == null) {
                    false
                } else {
                    when (mode) {
                        AdvancedFilterMode.GREATER_THAN -> fieldInt > inputInt
                        AdvancedFilterMode.GREATER_THAN_OR_EQUAL -> fieldInt >= inputInt
                        AdvancedFilterMode.LESS_THAN -> fieldInt < inputInt
                        AdvancedFilterMode.LESS_THAN_OR_EQUAL -> fieldInt <= inputInt
                        else -> throw RuntimeException("Invalid handling of filter modes.")
                    }
                }
            }
        }
    }

    val isValid: Boolean
        get() = selectedFieldIndex < fields.size && selectedModeIndex < modes.size && inputValue.isNotBlank()
}