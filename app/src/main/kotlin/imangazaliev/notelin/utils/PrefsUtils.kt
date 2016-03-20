package imangazaliev.notelin.utils

import android.content.Context
import android.content.SharedPreferences

class PrefsUtils {

    companion object {

        private lateinit var mPrefs: SharedPreferences

        fun init(context: Context) {
            mPrefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        }

        fun getNotesSortMethod(defaultValue: String): String = mPrefs.getString("sort_method", defaultValue)

        fun setNotesSortMethod(sortMethod: String) {
            mPrefs.edit().putString("sort_method", sortMethod).commit()
        }
    }

}
