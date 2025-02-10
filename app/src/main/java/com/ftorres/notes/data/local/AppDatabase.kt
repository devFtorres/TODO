package com.ftorres.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ftorres.notes.data.local.dao.NoteDao
import com.ftorres.notes.data.local.dao.UserDao
import com.ftorres.notes.data.local.entity.NoteEntity
import com.ftorres.notes.data.local.entity.UserEntity


@Database(
    entities = [NoteEntity::class, UserEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE notes ADD COLUMN dueDate INTEGER")
            }
        }
    }
}