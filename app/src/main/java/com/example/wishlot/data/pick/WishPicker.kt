package com.example.wishlot.data.pick

import com.example.wishlot.data.WishStatus
import kotlin.random.Random

object WishPicker {

    fun filterCandidates(wishes: List<WishSnapshot>, budgetMinor: Long): List<WishSnapshot> =
        wishes.filter { wish ->
            wish.status == WishStatus.ACTIVE && wish.priceMinor > 0 && wish.priceMinor <= budgetMinor
        }

    fun pickRandom(candidates: List<WishSnapshot>, random: Random = Random.Default): WishSnapshot? {
        if (candidates.isEmpty()) return null
        return candidates[random.nextInt(candidates.size)]
    }

    fun runPick(input: PickInput, random: Random = Random.Default): PickResult {
        val candidates = filterCandidates(input.wishes, input.budgetMinor)
        val winner = pickRandom(candidates, random) ?: return PickResult.NoCandidates
        return PickResult.Winner(winner)
    }
}
