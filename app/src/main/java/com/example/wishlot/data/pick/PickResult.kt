package com.example.wishlot.data.pick

sealed class PickResult {
    data object NoCandidates : PickResult()
    data class Winner(val wish: WishSnapshot) : PickResult()
}
