package com.example.wishlot.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WishDao {

    @Query("SELECT * FROM wishes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Wish?

    @Query(
        """
        SELECT * FROM wishes
        WHERE status = 'ACTIVE'
        ORDER BY
            CASE WHEN sortOrder IS NULL THEN 1 ELSE 0 END,
            sortOrder ASC,
            createdAt ASC,
            id ASC
        """,
    )
    fun observeActive(): Flow<List<Wish>>

    @Query(
        """
        SELECT * FROM wishes
        WHERE status = 'FULFILLED'
        ORDER BY fulfilledAt DESC, id DESC
        """,
    )
    fun observeFulfilled(): Flow<List<Wish>>

    @Query(
        """
        SELECT * FROM wishes
        WHERE status = 'ARCHIVED'
        ORDER BY createdAt DESC, id DESC
        """,
    )
    fun observeArchived(): Flow<List<Wish>>

    @Query("SELECT COALESCE(MAX(sortOrder), 0) FROM wishes WHERE status = 'ACTIVE'")
    suspend fun maxActiveSortOrder(): Int

    @Query("SELECT * FROM wishes")
    suspend fun getAll(): List<Wish>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(wish: Wish): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wishes: List<Wish>)

    @Update
    suspend fun update(wish: Wish)

    @Query("DELETE FROM wishes WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM wishes")
    suspend fun deleteAll()

    @Query(
        """
        UPDATE wishes SET status = 'FULFILLED', fulfilledAt = :fulfilledAt
        WHERE id = :id
        """,
    )
    suspend fun markFulfilled(id: Long, fulfilledAt: Long)

    @Query(
        """
        UPDATE wishes SET status = 'ARCHIVED', fulfilledAt = NULL
        WHERE id = :id
        """,
    )
    suspend fun markArchived(id: Long)

    @Query(
        """
        UPDATE wishes SET status = 'ACTIVE', fulfilledAt = NULL
        WHERE id = :id
        """,
    )
    suspend fun restoreToActive(id: Long)

    @Query("UPDATE wishes SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: Long, sortOrder: Int)
}
