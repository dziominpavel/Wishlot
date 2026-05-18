package com.example.wishlot.data.pick

import com.example.wishlot.data.WishPriority
import com.example.wishlot.data.WishStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class WishPickerTest {

    private val active1 = WishSnapshot(1, "A", 1_000, WishStatus.ACTIVE)
    private val active2 = WishSnapshot(2, "B", 3_000, WishStatus.ACTIVE)
    private val expensive = WishSnapshot(3, "C", 8_000, WishStatus.ACTIVE)
    private val fulfilled = WishSnapshot(4, "D", 500, WishStatus.FULFILLED)

    @Test
    fun filter_emptyList_returnsEmpty() {
        assertTrue(WishPicker.filterCandidates(emptyList(), 5_000).isEmpty())
    }

    @Test
    fun filter_allTooExpensive_returnsEmpty() {
        assertTrue(WishPicker.filterCandidates(listOf(expensive), 1_000).isEmpty())
    }

    @Test
    fun filter_includesExactBudget() {
        assertEquals(1, WishPicker.filterCandidates(listOf(active1), 1_000).size)
    }

    @Test
    fun filter_excludesFulfilled() {
        assertTrue(WishPicker.filterCandidates(listOf(fulfilled), 10_000).isEmpty())
    }

    @Test
    fun runPick_noCandidates() {
        assertEquals(PickResult.NoCandidates, WishPicker.runPick(PickInput(500, listOf(expensive))))
    }

    @Test
    fun runPick_singleCandidate() {
        val result = WishPicker.runPick(PickInput(5_000, listOf(active1, expensive)))
        assertTrue(result is PickResult.Winner)
        assertEquals(active1, (result as PickResult.Winner).wish)
    }

    @Test
    fun pickRandom_fixedSeed_isDeterministic() {
        val candidates = listOf(active1, active2)
        val first = WishPicker.pickRandom(candidates, Random(42))
        val second = WishPicker.pickRandom(candidates, Random(42))
        assertEquals(first, second)
    }

    @Test
    fun pickWeighted_highPriorityWinsMoreOften() {
        val low = WishSnapshot(1, "Low", 1_000, WishStatus.ACTIVE, priority = 1)
        val high = WishSnapshot(2, "High", 1_000, WishStatus.ACTIVE, priority = 3)
        val candidates = listOf(low, high)
        var highWins = 0
        repeat(1_000) { i ->
            val winner = WishPicker.pickWeighted(candidates, Random(i))
            if (winner?.id == high.id) highWins++
        }
        assertTrue("High priority should win > 60%", highWins > 600)
    }
}
