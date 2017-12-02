@file:JvmName("PrefsUtils")

package com.imangazaliev.notelin.utils

import android.content.Context
import com.imangazaliev.notelin.NotelinApplication

private val prefs by lazy {
    NotelinApplication.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
}

fun getNotesSortMethodName(defaultValue: String): String = prefs.getString("sort_method", defaultValue)

fun setNotesSortMethod(sortMethod: String) {
    prefs.edit().putString("sort_method", sortMethod).apply()
}
