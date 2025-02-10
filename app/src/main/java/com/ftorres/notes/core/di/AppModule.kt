package com.ftorres.notes.core.di

import android.app.Application
import androidx.room.Room
import com.ftorres.notes.data.local.AppDatabase
import com.ftorres.notes.data.local.dao.NoteDao
import com.ftorres.notes.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.mapbox.maps.MapView
import com.mapbox.maps.Style

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "notes_db"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideMapboxMap(application: Application): MapView {
        return MapView(application).apply {
            getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        }
    }
}
