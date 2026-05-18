package com.example.wishlot.data.backup

import com.example.wishlot.data.Wish
import com.example.wishlot.data.WishCategory
import com.example.wishlot.data.WishPriority
import com.example.wishlot.data.WishStatus
import org.json.JSONArray
import org.json.JSONObject

object BackupCodec {

    const val SCHEMA_VERSION = 1

    fun exportJson(wishes: List<Wish>): String {
        val root = JSONObject()
        root.put("schemaVersion", SCHEMA_VERSION)
        root.put("exportedAt", System.currentTimeMillis())
        val array = JSONArray()
        wishes.forEach { wish ->
            array.put(
                JSONObject().apply {
                    put("clientId", wish.clientId)
                    put("title", wish.title)
                    put("priceMinor", wish.priceMinor)
                    put("note", wish.note ?: JSONObject.NULL)
                    put("status", wish.status)
                    put("category", wish.category)
                    put("priority", wish.priority)
                    if (wish.sortOrder != null) put("sortOrder", wish.sortOrder)
                    put("createdAt", wish.createdAt)
                    if (wish.fulfilledAt != null) put("fulfilledAt", wish.fulfilledAt)
                },
            )
        }
        root.put("wishes", array)
        return root.toString(2)
    }

    fun importJson(json: String): List<Wish> {
        val root = JSONObject(json)
        val version = root.optInt("schemaVersion", 0)
        require(version in 1..SCHEMA_VERSION) { "Unsupported schema version: $version" }
        val array = root.getJSONArray("wishes")
        return buildList {
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                add(
                    Wish(
                        id = 0,
                        clientId = item.getString("clientId"),
                        title = item.getString("title"),
                        priceMinor = item.getLong("priceMinor"),
                        note = if (item.isNull("note")) null else item.optString("note").takeIf { it.isNotBlank() },
                        status = item.getString("status"),
                        category = WishCategory.fromName(item.optString("category", WishCategory.OTHER.name)).name,
                        priority = WishPriority.fromValue(item.optInt("priority", 1)).weight,
                        sortOrder = if (item.has("sortOrder") && !item.isNull("sortOrder")) {
                            item.getInt("sortOrder")
                        } else {
                            null
                        },
                        createdAt = item.getLong("createdAt"),
                        fulfilledAt = if (item.has("fulfilledAt") && !item.isNull("fulfilledAt")) {
                            item.getLong("fulfilledAt")
                        } else {
                            null
                        },
                    ),
                )
            }
        }.filter { wish ->
            WishStatus.entries.any { it.name == wish.status }
        }
    }
}
