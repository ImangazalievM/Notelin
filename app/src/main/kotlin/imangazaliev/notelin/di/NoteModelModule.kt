package imangazaliev.notelin.di

import dagger.Module
import dagger.Provides
import imangazaliev.notelin.mvp.models.NoteModel
import javax.inject.Singleton

@Module
class NoteModelModule {

    @Provides
    @Singleton
    fun provideNoteModel() : NoteModel = NoteModel()

}