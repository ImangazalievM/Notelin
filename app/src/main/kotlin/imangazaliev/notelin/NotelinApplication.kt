package imangazaliev.notelin

import android.content.Context
import com.activeandroid.app.Application
import imangazaliev.notelin.di.AppComponent
import imangazaliev.notelin.di.DaggerAppComponent
import imangazaliev.notelin.di.NoteWrapperModule
import imangazaliev.notelin.utils.PrefsUtils

class NotelinApplication : Application() {

    companion object {
        lateinit var graph: AppComponent
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        PrefsUtils.init(this)

        context = this
        graph = DaggerAppComponent.builder().noteModelModule(NoteWrapperModule()).build()
    }

}
