package com.ftorres.notes.core.di

import com.ftorres.notes.domain.repository.NotesRepository
import com.ftorres.notes.domain.usecase.notes.CreateNoteUseCase
import com.ftorres.notes.domain.usecase.notes.DeleteNoteUseCase
import com.ftorres.notes.domain.usecase.notes.GetNotesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideGetNotesUseCase(repository: NotesRepository): GetNotesUseCase {
        return GetNotesUseCase(repository)
    }

    @Provides
    fun provideCreateNoteUseCase(repository: NotesRepository): CreateNoteUseCase {
        return CreateNoteUseCase(repository)
    }

    @Provides
    fun provideDeleteNoteUseCase(repository: NotesRepository): DeleteNoteUseCase {
        return DeleteNoteUseCase(repository)
    }
}
