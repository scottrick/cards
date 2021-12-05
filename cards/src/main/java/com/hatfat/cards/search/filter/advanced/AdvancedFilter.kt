package com.hatfat.cards.search.filter.advanced

import java.io.Serializable

open class AdvancedFilter(
    open val fields: List<AdvancedFilterField>,
    open val modes: List<AdvancedFilterMode>,
    var selectedFieldIndex: Int = 0,
    var selectedModeIndex: Int = 0,
    var inputValue: String = ""
) : Serializable {

    /* for the given list of fields, determine if that value passes the filter */
    fun fieldsFilter(fields: List<String>): Boolean {
        return when (val mode = modes[selectedModeIndex]) {
            AdvancedFilterMode.CONTAINS -> {
                /* treat as strings */
                fields.forEach {
                    if (it.contains(inputValue, true)) {
                        return true
                    }
                }
                false
            }
            AdvancedFilterMode.DOES_NOT_CONTAIN -> {
                /* treat as strings */
                fields.forEach {
                    if (it.contains(inputValue, true)) {
                        return false
                    }
                }
                true
            }
            AdvancedFilterMode.EQUALS -> {
                /* compare all values, treat as strings */
                fields.forEach {
                    if (it.compareTo(inputValue, true) == 0) {
                        return true
                    }
                }
                false
            }
            AdvancedFilterMode.NOT_EQUALS -> {
                /* compare all values, treat as strings */
                fields.forEach {
                    if (it.compareTo(inputValue, true) == 0) {
                        return false
                    }
                }
                true
            }
            AdvancedFilterMode.LESS_THAN,
            AdvancedFilterMode.LESS_THAN_OR_EQUAL,
            AdvancedFilterMode.GREATER_THAN_OR_EQUAL,
            AdvancedFilterMode.GREATER_THAN -> {
                /* just take the first value, treat as number */
                val fieldValue = fields[0]
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