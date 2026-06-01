package com.example.wishlot.viewmodel

import com.example.wishlot.data.WishCategory

data class WishEditDraft(
    val id: Long? = null,
    val title: String = "",
    val priceInput: String = "",
    val note: String = "",
    val category: WishCategory = WishCategory.OTHER,
)

data class WishStats(
    val activeCount: Int = 0,
    val activeTotalMinor: Long = 0,
    val fulfilledCount: Int = 0,
    val fulfilledTotalMinor: Long = 0,
    val archivedCount: Int = 0,
)
