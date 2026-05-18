package com.example.wishlot.data

import androidx.compose.ui.graphics.Color
import com.example.wishlot.R
import com.example.wishlot.ui.theme.Coral
import com.example.wishlot.ui.theme.Gold
import com.example.wishlot.ui.theme.SuccessMint

enum class WishCategory(
    val labelRes: Int,
    val color: Color,
) {
    TECH(R.string.category_tech, Color(0xFF5C9CE6)),
    BOOKS(R.string.category_books, Color(0xFF9B7EDE)),
    FASHION(R.string.category_fashion, Coral),
    FOOD(R.string.category_food, Gold),
    OTHER(R.string.category_other, SuccessMint),
    ;

    companion object {
        fun fromName(name: String?): WishCategory =
            entries.find { it.name == name } ?: OTHER
    }
}
