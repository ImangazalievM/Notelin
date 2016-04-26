package imangazaliev.notelin.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {

        fun formatDate(date: Date?): String {
            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateFormat.format(date)
        }
    }

}

