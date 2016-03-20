package imangazaliev.notelin.mvp.common

import imangazaliev.notelin.mvp.models.Note
import java.util.*

/**
 * Cортировка заметок по дате
 */
class SortDate : Comparator<Note> {
    override fun compare(note1: Note, note2: Note): Int {
        return  note1.changeDate!!.compareTo(note2.changeDate!!)
    }

}