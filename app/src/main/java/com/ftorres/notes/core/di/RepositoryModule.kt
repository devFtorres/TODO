package com.ftorres.notes.core.di

import com.ftorres.notes.data.repository.AuthRepositoryImpl
import com.ftorres.notes.data.repository.NotesRepositoryImpl
import com.ftorres.notes.domain.repository.AuthRepository
import com.ftorres.notes.domain.repository.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNotesRepository(
        notesRepositoryImpl: NotesRepositoryImpl
    ): NotesRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
