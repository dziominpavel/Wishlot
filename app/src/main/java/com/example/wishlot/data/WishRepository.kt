package com.example.wishlot.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WishRepository(context: Context) {

    private val dao = AppDatabase.getInstance(context.applicationContext).wishDao()

    fun observeActive() = dao.observeActive()

    fun observeFulfilled() = dao.observeFulfilled()

    fun observeArchived() = dao.observeArchived()

    suspend fun getById(id: Long): Wish? = withContext(Dispatchers.IO) {
        dao.getById(id)
    }

    suspend fun getAll(): List<Wish> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    suspend fun insert(wish: Wish): Long = withContext(Dispatchers.IO) {
        dao.insert(wish)
    }

    suspend fun insertAll(wishes: List<Wish>) = withContext(Dispatchers.IO) {
        dao.insertAll(wishes)
    }

    suspend fun update(wish: Wish) = withContext(Dispatchers.IO) {
        dao.update(wish)
    }

    suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO) {
        dao.deleteAll()
    }

    suspend fun markFulfilled(id: Long) = withContext(Dispatchers.IO) {
        dao.markFulfilled(id, System.currentTimeMillis())
    }

    suspend fun markArchived(id: Long) = withContext(Dispatchers.IO) {
        dao.markArchived(id)
    }

    suspend fun restoreToActive(id: Long) = withContext(Dispatchers.IO) {
        dao.restoreToActive(id)
    }

    suspend fun nextSortOrder(): Int = withContext(Dispatchers.IO) {
        dao.maxActiveSortOrder() + 1
    }

    companion object {
        @Volatile
        private var instance: WishRepository? = null

        fun getInstance(context: Context): WishRepository =
            instance ?: synchronized(this) {
                instance ?: WishRepository(context).also { instance = it }
            }
    }
}
