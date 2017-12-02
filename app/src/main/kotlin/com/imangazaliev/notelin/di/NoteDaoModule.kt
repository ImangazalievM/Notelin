package com.imangazaliev.notelin.di

import dagger.Module
import dagger.Provides
import com.imangazaliev.notelin.mvp.model.NoteDao
import javax.inject.Singleton

@Module
class NoteDaoModule {

    @Provides
    @Singleton
    fun provideNoteDao(): NoteDao = NoteDao()

}