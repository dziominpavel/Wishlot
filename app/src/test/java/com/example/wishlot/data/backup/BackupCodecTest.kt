package com.example.wishlot.data.backup

import com.example.wishlot.data.Wish
import com.example.wishlot.data.WishCategory
import com.example.wishlot.data.WishStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupCodecTest {

    @Test
    fun exportImport_roundTrip() {
        val original = listOf(
            Wish(
                id = 1,
                clientId = "abc-123",
                title = "Headphones",
                priceMinor = 500_000L,
                note = "Sony",
                status = WishStatus.ACTIVE.name,
                category = WishCategory.TECH.name,
                priority = 2,
                sortOrder = 1,
                createdAt = 1_000L,
                fulfilledAt = null,
            ),
            Wish(
                id = 2,
                clientId = "def-456",
                title = "Book",
                priceMinor = 80_000L,
                note = null,
                status = WishStatus.FULFILLED.name,
                category = WishCategory.BOOKS.name,
                priority = 1,
                sortOrder = null,
                createdAt = 2_000L,
                fulfilledAt = 9_000L,
            ),
        )

        val json = BackupCodec.exportJson(original)
        val imported = BackupCodec.importJson(json)

        assertEquals(2, imported.size)
        assertEquals("Headphones", imported[0].title)
        assertEquals(WishCategory.TECH.name, imported[0].category)
        assertEquals(2, imported[0].priority)
        assertEquals(WishStatus.FULFILLED.name, imported[1].status)
        assertEquals(9_000L, imported[1].fulfilledAt)
        assertTrue(imported.all { it.id == 0L })
    }
}
