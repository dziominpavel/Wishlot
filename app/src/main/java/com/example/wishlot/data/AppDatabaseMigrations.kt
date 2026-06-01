package com.example.wishlot.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = Migration(1, 2) { db ->
    db.execSQL("ALTER TABLE wishes ADD COLUMN category TEXT NOT NULL DEFAULT 'OTHER'")
    db.execSQL("ALTER TABLE wishes ADD COLUMN priority INTEGER NOT NULL DEFAULT 1")
    db.execSQL("ALTER TABLE wishes ADD COLUMN sortOrder INTEGER")
}

val MIGRATION_2_3 = Migration(2, 3) { db ->
    db.execSQL(
        """
        CREATE TABLE wishes_new (
            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            clientId TEXT NOT NULL,
            title TEXT NOT NULL,
            priceMinor INTEGER NOT NULL,
            note TEXT,
            status TEXT NOT NULL,
            category TEXT NOT NULL DEFAULT 'OTHER',
            sortOrder INTEGER,
            createdAt INTEGER NOT NULL,
            fulfilledAt INTEGER
        )
        """.trimIndent(),
    )
    db.execSQL(
        """
        INSERT INTO wishes_new (id, clientId, title, priceMinor, note, status, category, sortOrder, createdAt, fulfilledAt)
        SELECT id, clientId, title, priceMinor, note, status, category, sortOrder, createdAt, fulfilledAt FROM wishes
        """.trimIndent(),
    )
    db.execSQL("DROP TABLE wishes")
    db.execSQL("ALTER TABLE wishes_new RENAME TO wishes")
    db.execSQL("CREATE INDEX index_wishes_status ON wishes(status)")
    db.execSQL("CREATE INDEX index_wishes_fulfilledAt ON wishes(fulfilledAt)")
    db.execSQL("CREATE INDEX index_wishes_sortOrder ON wishes(sortOrder)")
}
