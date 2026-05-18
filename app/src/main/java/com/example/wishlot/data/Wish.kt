package com.example.wishlot.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "wishes",
    indices = [
        Index(value = ["status"]),
        Index(value = ["fulfilledAt"]),
        Index(value = ["sortOrder"]),
    ],
)
data class Wish(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: String,
    val title: String,
    val priceMinor: Long,
    val note: String?,
    val status: String,
    val category: String = WishCategory.OTHER.name,
    val priority: Int = WishPriority.NORMAL.weight,
    val sortOrder: Int? = null,
    val createdAt: Long,
    val fulfilledAt: Long?,
)
