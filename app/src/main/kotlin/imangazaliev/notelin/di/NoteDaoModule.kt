package imangazaliev.notelin.di

import dagger.Module
import dagger.Provides
import imangazaliev.notelin.mvp.models.NoteDao
import javax.inject.Singleton

@Module
class NoteDaoModule {

    @Provides
    @Singleton
    fun provideNoteDao(): NoteDao = NoteDao()

}