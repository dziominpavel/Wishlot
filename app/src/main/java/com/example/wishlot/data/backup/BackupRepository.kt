package com.example.wishlot.data.backup

import android.content.Context
import com.example.wishlot.data.WishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackupRepository(context: Context) {

    private val repository = WishRepository.getInstance(context.applicationContext)

    suspend fun exportJson(): String = withContext(Dispatchers.IO) {
        BackupCodec.exportJson(repository.getAll())
    }

    suspend fun importJson(json: String, replaceAll: Boolean = true): Int = withContext(Dispatchers.IO) {
        val wishes = BackupCodec.importJson(json)
        if (replaceAll) {
            repository.deleteAll()
        }
        repository.insertAll(wishes)
        wishes.size
    }

    companion object {
        @Volatile
        private var instance: BackupRepository? = null

        fun getInstance(context: Context): BackupRepository =
            instance ?: synchronized(this) {
                instance ?: BackupRepository(context).also { instance = it }
            }
    }
}
