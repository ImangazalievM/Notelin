package com.imangazaliev.notelin

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.imangazaliev.notelin.di.AppComponent
import com.imangazaliev.notelin.di.DaggerAppComponent
import com.imangazaliev.notelin.di.NoteDaoModule
import com.imangazaliev.notelin.mvp.model.AppDatabase
import com.imangazaliev.notelin.mvp.model.Note
import com.reactiveandroid.ReActiveAndroid
import com.reactiveandroid.ReActiveConfig
import com.reactiveandroid.internal.database.DatabaseConfig

class NotelinApplication : Application() {

    companion object {
        lateinit var graph: AppComponent
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = this
        graph = DaggerAppComponent.builder().noteDaoModule(NoteDaoModule()).build()

        val appDatabaseConfig = DatabaseConfig.Builder(AppDatabase::class.java)
                .addModelClasses(Note::class.java)
                .build()

        ReActiveAndroid.init(ReActiveConfig.Builder(this)
                .addDatabaseConfigs(appDatabaseConfig)
                .build())
    }

}
