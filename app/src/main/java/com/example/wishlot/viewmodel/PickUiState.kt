package com.example.wishlot.viewmodel

import com.example.wishlot.data.Wish

sealed class PickUiState {
    data object Idle : PickUiState()
    data object NoCandidates : PickUiState()
    data class Spinning(
        val winner: Wish,
        val wheelItems: List<Wish>,
    ) : PickUiState()

    data class AwaitingDecision(val winner: Wish) : PickUiState()
}
