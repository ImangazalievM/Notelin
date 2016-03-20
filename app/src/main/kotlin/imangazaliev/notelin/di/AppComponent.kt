package imangazaliev.notelin.di

import dagger.Component
import imangazaliev.notelin.mvp.presenters.MainPresenter
import imangazaliev.notelin.mvp.presenters.NotePresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NoteModelModule::class))
interface AppComponent {
    fun inject(mainPresenter : MainPresenter)

    fun inject(mainPresenter : NotePresenter)
}