package com.example.wishlot.data.pick

import com.example.wishlot.data.Wish
import com.example.wishlot.data.WishStatus

data class WishSnapshot(
    val id: Long,
    val title: String,
    val priceMinor: Long,
    val status: WishStatus,
)

fun Wish.toSnapshot(): WishSnapshot = WishSnapshot(
    id = id,
    title = title,
    priceMinor = priceMinor,
    status = WishStatus.valueOf(status),
)

data class PickInput(
    val budgetMinor: Long,
    val wishes: List<WishSnapshot>,
)
