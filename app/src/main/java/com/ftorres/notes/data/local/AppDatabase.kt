package com.ftorres.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ftorres.notes.data.local.dao.NoteDao
import com.ftorres.notes.data.local.dao.UserDao
import com.ftorres.notes.data.local.entity.NoteEntity
import com.ftorres.notes.data.local.entity.UserEntity

@Database(
    entities = [NoteEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao
}
