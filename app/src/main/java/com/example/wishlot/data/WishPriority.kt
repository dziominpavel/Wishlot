package com.example.wishlot.data

import com.example.wishlot.R

enum class WishPriority(val weight: Int, val labelRes: Int) {
    NORMAL(1, R.string.priority_normal),
    HIGH(2, R.string.priority_high),
    HIGHEST(3, R.string.priority_highest),
    ;

    companion object {
        fun fromValue(value: Int): WishPriority =
            entries.find { it.weight == value.coerceIn(1, 3) } ?: NORMAL
    }
}
