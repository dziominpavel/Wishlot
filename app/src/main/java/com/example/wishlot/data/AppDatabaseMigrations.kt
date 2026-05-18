package com.example.wishlot.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = Migration(1, 2) { db ->
    db.execSQL("ALTER TABLE wishes ADD COLUMN category TEXT NOT NULL DEFAULT 'OTHER'")
    db.execSQL("ALTER TABLE wishes ADD COLUMN priority INTEGER NOT NULL DEFAULT 1")
    db.execSQL("ALTER TABLE wishes ADD COLUMN sortOrder INTEGER")
}
