package imangazaliev.notelin

import android.content.Context
import com.activeandroid.app.Application
import imangazaliev.notelin.di.AppComponent
import imangazaliev.notelin.di.DaggerAppComponent
import imangazaliev.notelin.di.NoteDaoModule
import imangazaliev.notelin.utils.initPrefs

class NotelinApplication : Application() {

    companion object {
        lateinit var graph: AppComponent
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        initPrefs(this)

        context = this
        graph = DaggerAppComponent.builder().noteDaoModule(NoteDaoModule()).build()
    }

}
