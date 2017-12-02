package com.imangazaliev.notelin.di

import dagger.Component
import com.imangazaliev.notelin.mvp.presenters.MainPresenter
import com.imangazaliev.notelin.mvp.presenters.NotePresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [NoteDaoModule::class])
interface AppComponent {
    fun inject(mainPresenter: MainPresenter)

    fun inject(notePresenter: NotePresenter)
}